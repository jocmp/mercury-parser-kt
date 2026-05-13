package com.jocmp.mercury.extractors.custom.www.cnet.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val WwwCnetComExtractor =
    extractor("www.cnet.com") {
        title { attr("meta[name=\"og:title\"]", "value") }

        author { selectors("span.author", "a.author") }

        datePublished {
            timezone = "America/Los_Angeles"
            selectors("time")
        }

        dek { selectors(".c-head_dek", ".article-dek") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream first selector is a 2-element compound array
            // ['img.__image-lead__', '.article-main-body']. Compound selectors
            // are not yet supported by the Kotlin DSL — falling through to the
            // scalar `.article-main-body`.
            selectors(".article-main-body")

            transform("figure.image") { node, _ ->
                val el = node.elements.firstOrNull() ?: return@transform TransformResult.NoChange
                val img = el.selectFirst("img") ?: return@transform TransformResult.NoChange
                img.attr("width", "100%")
                img.attr("height", "100%")
                img.addClass("__image-lead__")
                el.select(".imgContainer").remove()
                el.prependChild(img)
                TransformResult.NoChange
            }
        }
    }
