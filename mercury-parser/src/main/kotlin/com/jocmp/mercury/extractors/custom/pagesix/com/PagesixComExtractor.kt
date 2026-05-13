package com.jocmp.mercury.extractors.custom.pagesix.com

import com.jocmp.mercury.extractors.extractor

val PagesixComExtractor =
    extractor("pagesix.com") {
        supportedDomains = listOf("nypost.com")

        title { attr("meta[name=\"og:title\"]", "value") }

        author { selectors(".byline") }

        datePublished { attr("meta[name=\"article:published_time\"]", "value") }

        dek { attr("meta[name=\"description\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream first selector is a 2-element compound array
            // ['#featured-image-wrapper', '.entry-content']. Compound selectors are
            // not yet supported by the Kotlin DSL — falling through to scalar `.entry-content`.
            selectors(".entry-content")

            transform("#featured-image-wrapper", renameTo = "figure")
            transform(".wp-caption-text", renameTo = "figcaption")

            clean(".modal-trigger")
        }
    }
