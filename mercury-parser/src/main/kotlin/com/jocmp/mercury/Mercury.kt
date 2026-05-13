package com.jocmp.mercury

import com.jocmp.mercury.extractors.generic.GenericExtractor
import com.jocmp.mercury.resource.Resource
import com.jocmp.mercury.utils.validateUrl
import java.net.URI

object Mercury {
    suspend fun parse(
        url: String,
        options: ParseOptions = ParseOptions(),
    ): ParseResult {
        if (options.contentType == ContentType.MARKDOWN || options.contentType == ContentType.TEXT) {
            throw NotImplementedError("Only ContentType.HTML is currently supported")
        }

        val parsedUrl =
            try {
                URI.create(url)
            } catch (_: Throwable) {
                return ParseResult(
                    url = url,
                    error = true,
                    message = "The url parameter passed does not look like a valid URL. Please check your URL and try again.",
                )
            }

        if (!validateUrl(parsedUrl)) {
            return ParseResult(
                url = url,
                error = true,
                message = "The url parameter passed does not look like a valid URL. Please check your URL and try again.",
            )
        }

        val doc =
            try {
                Resource.create(
                    url = url,
                    preparedResponse = options.html,
                    parsedUrl = parsedUrl,
                    headers = options.headers,
                    client = options.httpClient,
                )
            } catch (e: Throwable) {
                return ParseResult(url = url, error = true, message = e.message)
            }

        return GenericExtractor.extract(html = options.html ?: doc.html(), docIn = doc, url = url)
    }
}
