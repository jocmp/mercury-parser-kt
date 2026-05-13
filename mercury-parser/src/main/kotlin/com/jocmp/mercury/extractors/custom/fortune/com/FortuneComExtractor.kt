package com.jocmp.mercury.extractors.custom.fortune.com

import com.jocmp.mercury.extractors.extractor

val FortuneComExtractor =
    extractor("fortune.com") {
        title { selectors("h1") }

        author { attr("meta[name=\"author\"]", "value") }

        datePublished {
            selectors(".MblGHNMJ")
            // timezone: 'UTC' (per-field timezone option not plumbed through DSL yet)
        }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream first selector is a 2-element compound array ['picture', 'article.row']
            // (both must match, results concatenated). Compound selectors are not yet
            // supported by the Kotlin DSL — falling through to the scalar 'article.row'.
            selectors("article.row")

            // Is there anything in the content you selected that needs transformed
            // before it's consumable content? E.g., unusual lazy loaded images

            // Is there anything that is in the result that shouldn't be?
            // The clean selectors will remove anything that matches from
            // the result
        }
    }
