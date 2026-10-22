# JS engine benchmark

Compares parsing the same fixtures on Android with:

- **kotlin**: `com.jocmp:mercury-parser` (Jsoup, in-process)
- **sandbox**: upstream mercury-parser's JS in `androidx.javascriptengine` (V8 in the WebView sandbox process)
- **quickjs**: the same JS bundle in `io.github.dokar3:quickjs-kt` (in-process interpreter)

`js/build.mjs` bundles upstream `src/mercury.js` with esbuild for a bare engine: core-js `URL`, `URLSearchParams`, `atob`, `btoa`, a stub for `postman-request`, and upstream's `iconv-lite` shim. Each engine runs in a fresh instrumentation process; `run.py` samples `/proc/<pid>/status` for the app and sandbox processes.

## Run

```
python3 run.py --mercury-dir ../../mercury-parser
python3 run.py --skip-build kotlin sandbox
```

Needs a connected device or emulator, Node, and `local.properties` with `sdk.dir`. Results land in `results/`.

## 2026-10-21, sdk_gphone16k_arm64 emulator (API 37, WebView 155)

Median parse time. kotlin and sandbox: 2 warmup passes, 8 measured. quickjs: 1 warmup, 3 measured.

| case | kotlin | sandbox | quickjs |
|---|---|---|---|
| theatlantic (36 KB) | 17 ms | 121 ms | 1094 ms |
| apartmenttherapy (93 KB) | 35 ms | 263 ms | 2180 ms |
| apartmenttherapy, generic (93 KB) | 36 ms | 253 ms | 1671 ms |
| deadline (171 KB) | 50 ms | 417 ms | 3496 ms |
| deadline, generic (171 KB) | 75 ms | 228 ms | 2921 ms |
| newrepublic (269 KB) | 38 ms | 259 ms | 2551 ms |
| newrepublic, generic (269 KB) | 42 ms | 215 ms | 3132 ms |
| reddit (2593 KB) | 251 ms | 2568 ms | 25907 ms |
| reddit, generic (2593 KB) | 4975 ms | 9429 ms | 137310 ms |

| engine | init | first parse | app peak anon | sandbox peak anon | app peak RSS | sandbox peak RSS |
|---|---|---|---|---|---|---|
| idle baseline | - | - | 36 MB | - | 96 MB | - |
| kotlin | 0 ms | 134 ms | 168 MB | - | 237 MB | - |
| sandbox | 353 ms | 308 ms | 85 MB | 181 MB | 149 MB | 272 MB |
| quickjs | 211 ms | 685 ms | 138 MB | - | 201 MB | - |
