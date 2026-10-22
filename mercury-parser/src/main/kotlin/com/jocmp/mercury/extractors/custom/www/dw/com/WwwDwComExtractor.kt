package com.jocmp.mercury.extractors.custom.www.dw.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

private const val FORMAT_ID_TOKEN = "\${formatId}"

val WwwDwComExtractor =
    extractor("www.dw.com") {
        title { attr("meta[name=\"og:title\"]", "value") }

        author { selectors(".author-name .author-link") }

        datePublished { attr("meta[name=\"date\"]", "value") }

        dek { attr("meta[name=\"og:description\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors("[data-tracking-name=\"rich-text\"]")

            transform("img") { node, _ ->
                val template = node.attr("data-url") ?: node.attr("src") ?: ""
                if (template.contains(FORMAT_ID_TOKEN)) {
                    node.attr("src", template.replaceFirst(FORMAT_ID_TOKEN, "6"))
                }
                TransformResult.NoChange
            }

            clean("blockquote.tweet.embed")
        }
    }
