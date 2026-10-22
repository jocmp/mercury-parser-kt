package com.jocmp.mercury.bench

import android.os.Debug
import android.os.SystemClock
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.junit.Test
import org.junit.runner.RunWith

private const val TAG = "MercuryBench"
private const val GENERIC_URL = "https://bench.invalid/article"
private const val WARMUP_PASSES = 2
private const val MEASURED_ITERATIONS = 8

data class BenchCase(
    val name: String,
    val asset: String,
    val url: String,
)

private val CASES =
    listOf(
        BenchCase("theatlantic", "fixtures/www.theatlantic.com.html", "https://www.theatlantic.com/"),
        BenchCase("apartmenttherapy", "fixtures/www.apartmenttherapy.com.html", "https://www.apartmenttherapy.com/"),
        BenchCase("apartmenttherapy-generic", "fixtures/www.apartmenttherapy.com.html", GENERIC_URL),
        BenchCase("deadline", "fixtures/deadline.com.html", "https://deadline.com/"),
        BenchCase("deadline-generic", "fixtures/deadline.com.html", GENERIC_URL),
        BenchCase("newrepublic", "fixtures/newrepublic.com--minutes.html", "https://newrepublic.com/"),
        BenchCase("newrepublic-generic", "fixtures/newrepublic.com--minutes.html", GENERIC_URL),
        BenchCase("reddit", "fixtures/www.reddit.com--image.html", "https://www.reddit.com/"),
        BenchCase("reddit-generic", "fixtures/www.reddit.com--image.html", GENERIC_URL),
    )

@RunWith(AndroidJUnit4::class)
class ParseBenchmark {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val appContext = instrumentation.targetContext
    private val testContext = instrumentation.context
    private val arguments = InstrumentationRegistry.getArguments()
    private val warmupPasses = arguments.getString("warmup")?.toInt() ?: WARMUP_PASSES
    private val iterations = arguments.getString("iterations")?.toInt() ?: MEASURED_ITERATIONS

    @Test
    fun baseline() {
        phase("baseline", "idle")
        SystemClock.sleep(3_000)
        phase("baseline", "done")
        report("baseline", buildJsonObject { putMemory() })
    }

    @Test
    fun kotlin() = runBlocking { measure { KotlinEngine() } }

    @Test
    fun sandbox() = runBlocking { measure { SandboxEngine.create(appContext) } }

    @Test
    fun quickjs() = runBlocking { measure { QuickJsEngine.create(appContext) } }

    private suspend fun measure(create: suspend () -> Engine) {
        val inputs = CASES.associateWith { readAsset(it.asset) }

        phase("engine", "init")
        val initStart = SystemClock.elapsedRealtimeNanos()
        val engine = create()
        val initMs = elapsedMs(initStart)

        engine.use {
            phase(engine.name, "first-parse")
            val first = CASES.first()
            val firstStart = SystemClock.elapsedRealtimeNanos()
            engine.parse(first.url, inputs.getValue(first))
            val firstParseMs = elapsedMs(firstStart)

            phase(engine.name, "warmup")
            repeat(warmupPasses) {
                CASES.forEach { engine.parse(it.url, inputs.getValue(it)) }
            }

            phase(engine.name, "measure")
            CASES.forEach { case ->
                val html = inputs.getValue(case)
                val timings = mutableListOf<Double>()
                var summary: Summary? = null
                repeat(iterations) {
                    val start = SystemClock.elapsedRealtimeNanos()
                    summary = engine.parse(case.url, html)
                    timings += elapsedMs(start)
                }
                val sorted = timings.sorted()
                report(
                    "case",
                    buildJsonObject {
                        put("engine", engine.name)
                        put("case", case.name)
                        put("html_kb", html.length / 1024)
                        put("min_ms", sorted.first())
                        put("median_ms", sorted[sorted.size / 2])
                        put("max_ms", sorted.last())
                        put("title", summary?.title)
                        put("word_count", summary?.wordCount)
                        put("content_length", summary?.contentLength)
                    },
                )
            }

            phase(engine.name, "hold")
            SystemClock.sleep(2_000)

            report(
                "engine",
                buildJsonObject {
                    put("engine", engine.name)
                    put("init_ms", initMs)
                    put("first_parse_ms", firstParseMs)
                    engine.jsHeapBytes()?.let { put("js_heap_peak_kb", it / 1024) }
                    putJsonObject("notes") { engine.notes.forEach { (key, value) -> put(key, value) } }
                    putMemory()
                },
            )
        }
        phase(engine.name, "done")
    }

    private fun kotlinx.serialization.json.JsonObjectBuilder.putMemory() {
        val runtime = Runtime.getRuntime()
        put("app_pss_kb", Debug.getPss())
        put("app_java_heap_kb", (runtime.totalMemory() - runtime.freeMemory()) / 1024)
        put("app_native_heap_kb", Debug.getNativeHeapAllocatedSize() / 1024)
    }

    private fun readAsset(path: String): String = testContext.assets.open(path).bufferedReader().use { it.readText() }

    private fun elapsedMs(startNanos: Long): Double = (SystemClock.elapsedRealtimeNanos() - startNanos) / 1_000_000.0

    private fun phase(
        engine: String,
        name: String,
    ) {
        Log.i(TAG, "BENCH_PHASE $engine $name ${SystemClock.elapsedRealtime()}")
    }

    private fun report(
        kind: String,
        body: kotlinx.serialization.json.JsonObject,
    ) {
        Log.i(TAG, "BENCH_${kind.uppercase()} $body")
    }
}
