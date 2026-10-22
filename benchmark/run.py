#!/usr/bin/env python3
import argparse
import json
import pathlib
import re
import shutil
import subprocess
import sys
import time

HERE = pathlib.Path(__file__).resolve().parent
FIXTURES = HERE.parent / "mercury-parser" / "src" / "test" / "resources" / "fixtures"
FIXTURE_FILES = [
    "www.theatlantic.com.html",
    "www.apartmenttherapy.com.html",
    "deadline.com.html",
    "newrepublic.com--minutes.html",
    "www.reddit.com--image.html",
]
APP_ID = "com.jocmp.mercury.bench"
RUNNER = f"{APP_ID}.test/androidx.test.runner.AndroidJUnitRunner"
TEST_CLASS = f"{APP_ID}.ParseBenchmark"
SANDBOX_PATTERN = "sandboxed_process"

SAMPLER = (
    "while true; do "
    f"for p in $(pidof {APP_ID}) $(pgrep -f {SANDBOX_PATTERN}); do "
    "n=$(tr '\\0' ' ' < /proc/$p/cmdline 2>/dev/null); "
    "s=$(grep -E '^(VmRSS|VmHWM|RssAnon)' /proc/$p/status 2>/dev/null | tr -s ' \\t' ' ' | tr '\\n' ' '); "
    'echo "S $p|$n|$s"; '
    "done; sleep 0.25; done"
)

ENGINES = {
    "baseline": {},
    "kotlin": {},
    "sandbox": {},
    "quickjs": {"iterations": "3", "warmup": "1"},
}


def run(cmd, **kwargs):
    return subprocess.run(cmd, check=True, text=True, **kwargs)


def adb(*args, **kwargs):
    return run(["adb", *args], capture_output=True, **kwargs).stdout


def prepare(mercury_dir):
    assets = HERE / "app" / "src" / "androidTest" / "assets" / "fixtures"
    assets.mkdir(parents=True, exist_ok=True)
    for name in FIXTURE_FILES:
        shutil.copy(FIXTURES / name, assets / name)

    env_dir = ["env", f"MERCURY_PARSER_DIR={mercury_dir}"] if mercury_dir else []
    run([*env_dir, "node", str(HERE / "js" / "build.mjs")])
    run(["./gradlew", "-q", ":app:assembleDebug", ":app:assembleDebugAndroidTest"], cwd=HERE)
    adb("install", "-r", "-t", str(HERE / "app/build/outputs/apk/debug/app-debug.apk"))
    adb("install", "-r", "-t", str(HERE / "app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"))


def parse_status(text):
    values = {}
    for key, value in re.findall(r"(VmRSS|VmHWM|RssAnon): (\d+) kB", text):
        values[key] = int(value)
    return values


def run_engine(engine, extra_args):
    adb("shell", "am", "force-stop", APP_ID)
    adb("logcat", "-c")
    time.sleep(1)

    sampler = subprocess.Popen(["adb", "shell", SAMPLER], stdout=subprocess.PIPE, text=True)
    args = ["shell", "am", "instrument", "-w", "-e", "class", f"{TEST_CLASS}#{engine}"]
    for key, value in extra_args.items():
        args += ["-e", key, value]
    args.append(RUNNER)
    started = time.time()
    output = adb(*args)
    elapsed = time.time() - started
    sampler.terminate()
    samples, _ = sampler.communicate()

    if "OK (1 test)" not in output:
        print(output, file=sys.stderr)
        raise SystemExit(f"{engine} failed")

    processes = {}
    for line in samples.splitlines():
        if not line.startswith("S "):
            continue
        _, name, status = line[2:].split("|", 2)
        name = name.strip()
        if name.startswith("/system/bin/sh"):
            continue
        if APP_ID in name:
            role = "app"
        else:
            role = "sandbox"
        stats = parse_status(status)
        peak = processes.setdefault(role, {"name": name, "peak_rss_kb": 0, "peak_anon_kb": 0, "hwm_kb": 0})
        peak["peak_rss_kb"] = max(peak["peak_rss_kb"], stats.get("VmRSS", 0))
        peak["peak_anon_kb"] = max(peak["peak_anon_kb"], stats.get("RssAnon", 0))
        peak["hwm_kb"] = max(peak["hwm_kb"], stats.get("VmHWM", 0))

    cases = []
    summary = {}
    for line in adb("logcat", "-d", "-s", "MercuryBench").splitlines():
        match = re.search(r"BENCH_(CASE|ENGINE|BASELINE) (\{.*\})$", line)
        if not match:
            continue
        body = json.loads(match.group(2))
        if match.group(1) == "CASE":
            cases.append(body)
        else:
            summary = body

    return {
        "engine": engine,
        "wall_s": round(elapsed, 1),
        "processes": processes,
        "summary": summary,
        "cases": cases,
    }


def megabytes(process, key):
    if not process:
        return "-"
    return f"{process[key] / 1024:.0f}"


def milliseconds(summary, key):
    if key not in summary:
        return "-"
    return f"{summary[key]:.0f}"


def summarize(results):
    timed = [result for result in results if result["cases"]]
    lines = ["| case | " + " | ".join(result["engine"] for result in timed) + " |"]
    lines.append("|---" * (len(timed) + 1) + "|")
    for index, case in enumerate(timed[0]["cases"]):
        cells = [f'{case["case"]} ({case["html_kb"]} KB)']
        for result in timed:
            cells.append(f'{result["cases"][index]["median_ms"]:.0f} ms')
        lines.append("| " + " | ".join(cells) + " |")

    lines.append("")
    lines.append(
        "| engine | init ms | first parse ms | app peak anon MB | sandbox peak anon MB | app peak RSS MB | sandbox peak RSS MB |"
    )
    lines.append("|---|---|---|---|---|---|---|")
    for result in results:
        app = result["processes"].get("app")
        sandbox = result["processes"].get("sandbox")
        cells = [
            result["engine"],
            milliseconds(result["summary"], "init_ms"),
            milliseconds(result["summary"], "first_parse_ms"),
            megabytes(app, "peak_anon_kb"),
            megabytes(sandbox, "peak_anon_kb"),
            megabytes(app, "peak_rss_kb"),
            megabytes(sandbox, "peak_rss_kb"),
        ]
        lines.append("| " + " | ".join(cells) + " |")
    return "\n".join(lines)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--mercury-dir", help="mercury-parser checkout to bundle from")
    parser.add_argument("--skip-build", action="store_true")
    parser.add_argument("engines", nargs="*", default=list(ENGINES))
    options = parser.parse_args()

    if not options.skip_build:
        prepare(options.mercury_dir)

    results = []
    for engine in options.engines:
        print(f"running {engine}...", flush=True)
        results.append(run_engine(engine, ENGINES[engine]))

    out = HERE / "results"
    out.mkdir(exist_ok=True)
    stamp = time.strftime("%Y%m%d-%H%M%S")
    device = adb("shell", "getprop", "ro.product.model").strip()
    payload = {"device": device, "results": results}
    (out / f"{stamp}.json").write_text(json.dumps(payload, indent=2))
    print(f"device: {device}")
    print(summarize(results))


if __name__ == "__main__":
    main()
