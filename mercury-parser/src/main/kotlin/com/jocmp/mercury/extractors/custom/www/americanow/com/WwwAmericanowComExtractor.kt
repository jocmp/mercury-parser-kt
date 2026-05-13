package com.jocmp.mercury.extractors.custom.www.americanow.com

import com.jocmp.mercury.extractors.extractor

val WwwAmericanowComExtractor =
    extractor("www.americanow.com") {
        title {
            selector(".title")
            attr("meta[name=\"title\"]", "value")
        }

        author { selectors(".byline") }

        datePublished { attr("meta[name=\"publish_date\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream first selector is a 3-element compound array
            // ['.article-content', '.image', '.body']. Compound selectors are not
            // yet supported by the Kotlin DSL — falling through to scalar `.body`.
            selectors(".body")

            clean(".article-video-wrapper", ".show-for-small-only")
        }
    }
