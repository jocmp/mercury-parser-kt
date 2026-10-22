package com.jocmp.mercury.extractors.custom.substack.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val SubstackComExtractor =
    extractor("substack.com") {
        title { attr("meta[name=\"og:title\"]", "value") }

        author { attr("meta[name=\"author\"]", "value") }

        datePublished { attr("meta[name=\"article:published_time\"]", "value") }

        dek { attr("meta[name=\"og:description\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors(".available-content")

            transform("div.captioned-image-container", renameTo = "figure")

            transform("div.image-link") { node, _ ->
                val img = node.find("img")
                if (img.length == 0) {
                    node.remove()
                } else {
                    node.replaceWith(img.outerHtml())
                }
                TransformResult.NoChange
            }

            clean(
                ".subscribe-widget",
                ".subscription-widget-wrap",
                ".subscription-widget-wrap-editor",
                ".button-wrapper",
                ".poll-embed",
                ".share-dialog",
            )
        }
    }
