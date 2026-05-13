package com.jocmp.mercury.extractors.custom.www.si.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val WwwSiComExtractor =
    extractor("www.si.com") {
        title { selectors("h1", "h1.headline") }

        author { attr("meta[name=\"author\"]", "value") }

        datePublished {
            attr("meta[name=\"published\"]", "value")
            timezone = "America/New_York"
        }

        dek { selectors(".m-detail-header--dek") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream's second content selector is a compound `['p', '.marquee_large_2x',
            // '.component.image']` which the DSL doesn't yet support; scalar fallback used.
            selectors(".m-detail--body")

            transform("noscript") { node, _ ->
                val el = node.elements.firstOrNull() ?: return@transform TransformResult.NoChange
                val children = el.children()
                if (children.size == 1 && children[0].tagName() == "img") {
                    return@transform TransformResult.Rename("figure")
                }
                TransformResult.NoChange
            }

            // Upstream's clean list is a single compound selector `['.inline-thumb', ...]`
            // which the DSL doesn't yet support; we skip it for now.
        }
    }
