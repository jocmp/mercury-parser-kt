package com.jocmp.mercury.bench

import android.content.Context
import androidx.javascriptengine.IsolateStartupParameters
import androidx.javascriptengine.JavaScriptIsolate
import androidx.javascriptengine.JavaScriptSandbox
import com.dokar.quickjs.QuickJs
import com.dokar.quickjs.binding.FunctionBinding
import com.jocmp.mercury.Mercury
import com.jocmp.mercury.ParseOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.Closeable

private const val HEAP_LIMIT_BYTES = 512L * 1024 * 1024
private const val BUNDLE_ASSET = "mercury.bundle.js"

data class Summary(
    val title: String?,
    val wordCount: Int?,
    val contentLength: Int,
)

interface Engine : Closeable {
    val name: String

    val notes: Map<String, String>

    suspend fun parse(
        url: String,
        html: String,
    ): Summary

    fun jsHeapBytes(): Long? = null
}

class KotlinEngine : Engine {
    override val name = "kotlin"

    override val notes = mapOf("library" to "com.jocmp:mercury-parser:0.1.2")

    override suspend fun parse(
        url: String,
        html: String,
    ): Summary {
        val result = Mercury.parse(url, ParseOptions(html = html, fetchAllPages = false))
        return Summary(
            title = result.title,
            wordCount = result.wordCount,
            contentLength = result.content?.length ?: 0,
        )
    }

    override fun close() = Unit
}

class SandboxEngine private constructor(
    private val sandbox: JavaScriptSandbox,
    private val isolate: JavaScriptIsolate,
    override val notes: Map<String, String>,
) : Engine {
    override val name = "sandbox"

    override suspend fun parse(
        url: String,
        html: String,
    ): Summary {
        val code = "mercuryParse(${quote(url)}, ${quote(html)})"
        return summarize(isolate.evaluateJavaScriptAsync(code).get())
    }

    override fun close() {
        isolate.close()
        sandbox.close()
    }

    companion object {
        fun create(context: Context): SandboxEngine {
            check(JavaScriptSandbox.isSupported()) { "JavaScriptSandbox is not supported on this device" }

            val sandbox = JavaScriptSandbox.createConnectedInstanceAsync(context).get()
            val features =
                listOf(
                    JavaScriptSandbox.JS_FEATURE_PROMISE_RETURN,
                    JavaScriptSandbox.JS_FEATURE_ISOLATE_MAX_HEAP_SIZE,
                    JavaScriptSandbox.JS_FEATURE_EVALUATE_WITHOUT_TRANSACTION_LIMIT,
                    JavaScriptSandbox.JS_FEATURE_EVALUATE_FROM_FD,
                ).associateWith { sandbox.isFeatureSupported(it) }

            check(features.getValue(JavaScriptSandbox.JS_FEATURE_PROMISE_RETURN)) { "promise return unsupported" }

            val params = IsolateStartupParameters()
            params.maxEvaluationReturnSizeBytes = 64 * 1024 * 1024
            if (features.getValue(JavaScriptSandbox.JS_FEATURE_ISOLATE_MAX_HEAP_SIZE)) {
                params.maxHeapSizeBytes = HEAP_LIMIT_BYTES
            }

            val isolate = sandbox.createIsolate(params)
            if (features.getValue(JavaScriptSandbox.JS_FEATURE_EVALUATE_FROM_FD)) {
                context.assets.openFd(BUNDLE_ASSET).use { fd ->
                    isolate.evaluateJavaScriptAsync(fd).get()
                }
            } else {
                isolate.evaluateJavaScriptAsync(readBundle(context)).get()
            }

            val notes = features.mapKeys { (key, _) -> key.removePrefix("JS_FEATURE_").lowercase() }.mapValues { it.value.toString() }
            return SandboxEngine(sandbox, isolate, notes)
        }
    }
}

class QuickJsEngine private constructor(
    private val quickJs: QuickJs,
    private val input: Input,
) : Engine {
    override val name = "quickjs"

    override val notes = mapOf("version" to quickJs.version)

    private var peakHeap = 0L

    override suspend fun parse(
        url: String,
        html: String,
    ): Summary {
        input.url = url
        input.html = html
        quickJs.evaluate<Any?>(
            "globalThis.__benchOut = null; mercuryParse(__benchUrl(), __benchHtml()).then(r => { globalThis.__benchOut = r; });",
            "parse.js",
            false,
        )
        val json = quickJs.evaluate<String?>("globalThis.__benchOut", "result.js", false)
        checkNotNull(json) { "parse promise did not settle" }
        peakHeap = maxOf(peakHeap, quickJs.memoryUsage.memoryUsedSize)
        input.html = ""
        return summarize(json)
    }

    override fun jsHeapBytes(): Long = peakHeap

    override fun close() = quickJs.close()

    class Input {
        var url: String = ""
        var html: String = ""
    }

    companion object {
        suspend fun create(context: Context): QuickJsEngine {
            val quickJs = QuickJs.create(Dispatchers.Default)
            quickJs.memoryLimit = HEAP_LIMIT_BYTES
            quickJs.maxStackSize = 8L * 1024 * 1024

            val input = Input()
            quickJs.defineBinding("__benchUrl", FunctionBinding<String> { input.url })
            quickJs.defineBinding("__benchHtml", FunctionBinding<String> { input.html })
            quickJs.evaluate<Any?>(readBundle(context), BUNDLE_ASSET, false)

            return QuickJsEngine(quickJs, input)
        }
    }
}

private fun readBundle(context: Context): String = context.assets.open(BUNDLE_ASSET).bufferedReader().use { it.readText() }

private fun quote(value: String): String = JsonPrimitive(value).toString()

private fun summarize(json: String): Summary {
    val result = Json.parseToJsonElement(json).jsonObject
    return Summary(
        title = result["title"]?.jsonPrimitive?.contentOrNull,
        wordCount = result["word_count"]?.jsonPrimitive?.intOrNull,
        contentLength = result["content"]?.jsonPrimitive?.contentOrNull?.length ?: 0,
    )
}
