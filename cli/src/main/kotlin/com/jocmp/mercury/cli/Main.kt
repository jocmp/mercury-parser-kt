package com.jocmp.mercury.cli

import com.jocmp.mercury.ContentType
import com.jocmp.mercury.Mercury
import com.jocmp.mercury.ParseOptions
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.system.exitProcess

private const val USAGE = """Usage: mercury --url <URL> [options]

Options:
  --url URL              URL of the article to parse (required)
  --html FILE            Path to a local HTML file to parse instead of fetching
  --format html|markdown|text   Output format (default: html)
  --header KEY=VAL       Add a request header (repeatable)
  --no-fallback          Disable generic-extractor fallback
  --no-all-pages         Do not collect multi-page articles
  --help, -h             Show this message
"""

fun main(args: Array<String>) {
    val parsed = parseArgs(args)
    val url = parsed.url ?: run { fail("--url is required") }
    val html = parsed.htmlFile?.let { File(it).readText() }

    val options = ParseOptions(
        html = html,
        fetchAllPages = parsed.allPages,
        fallback = parsed.fallback,
        contentType = parsed.format,
        headers = parsed.headers,
    )

    val result = runBlocking { Mercury.parse(url, options) }
    if (result.error) {
        System.err.println(result.message ?: "Unknown error")
        exitProcess(1)
    }
    println(result.content ?: "")
}

private data class ParsedArgs(
    val url: String? = null,
    val htmlFile: String? = null,
    val format: ContentType = ContentType.HTML,
    val headers: Map<String, String> = emptyMap(),
    val fallback: Boolean = true,
    val allPages: Boolean = true,
)

private fun parseArgs(args: Array<String>): ParsedArgs {
    var i = 0
    var url: String? = null
    var htmlFile: String? = null
    var format = ContentType.HTML
    val headers = mutableMapOf<String, String>()
    var fallback = true
    var allPages = true

    while (i < args.size) {
        when (val a = args[i]) {
            "--help", "-h" -> { print(USAGE); exitProcess(0) }
            "--url" -> url = args.getOrNull(++i) ?: fail("--url requires a value")
            "--html" -> htmlFile = args.getOrNull(++i) ?: fail("--html requires a value")
            "--format" -> format = parseFormat(args.getOrNull(++i) ?: fail("--format requires a value"))
            "--header" -> {
                val pair = args.getOrNull(++i) ?: fail("--header requires KEY=VAL")
                val eq = pair.indexOf('=').takeIf { it > 0 } ?: fail("--header expects KEY=VAL, got: $pair")
                headers[pair.substring(0, eq)] = pair.substring(eq + 1)
            }
            "--no-fallback" -> fallback = false
            "--no-all-pages" -> allPages = false
            else -> fail("Unknown argument: $a")
        }
        i++
    }
    return ParsedArgs(url, htmlFile, format, headers, fallback, allPages)
}

private fun parseFormat(raw: String): ContentType = when (raw.lowercase()) {
    "html" -> ContentType.HTML
    "markdown" -> ContentType.MARKDOWN
    "text" -> ContentType.TEXT
    else -> fail("--format must be one of: html, markdown, text")
}

private fun fail(msg: String): Nothing {
    System.err.println(msg)
    System.err.println()
    System.err.println(USAGE)
    exitProcess(2)
}
