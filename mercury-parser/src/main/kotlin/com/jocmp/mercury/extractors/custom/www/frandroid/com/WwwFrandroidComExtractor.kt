package com.jocmp.mercury.extractors.custom.www.frandroid.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val WwwFrandroidComExtractor =
    extractor("www.frandroid.com") {
        title { attr("meta[name=\"og:title\"]", "value") }

        author { attr("meta[name=\"parsely-author\"]", "value") }

        datePublished { attr("meta[name=\"article:published_time\"]", "value") }

        dek { attr("meta[name=\"og:description\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors("section.article-content")

            transform("h2") { node, _ ->
                node.attr("class", "mercury-parser-keep")
                TransformResult.NoChange
            }

            transform("h3") { node, _ ->
                node.attr("class", "mercury-parser-keep")
                TransformResult.NoChange
            }

            clean(
                ".index-menu-wrapper",
                ".is-gastric-kingfisher",
                ".newsletter-form",
                ".share",
                ".article-footer",
                ".js-feed-posts",
                ".optidigital-adslot",
                "[id^=\"optidigital-adslot\"]",
            )
        }
    }
