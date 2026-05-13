package com.jocmp.mercury.extractors.custom.www.channelnewsasia.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val WwwChannelnewsasiaComExtractor =
    extractor("www.channelnewsasia.com") {
        title { attr("meta[name=\"og:title\"]", "value") }

        author {
            selector(".link--author-profile")
            attr("meta[name=\"cXenseParse:author\"]", "value")
        }

        datePublished {
            // Upstream also specifies `format: 'D MMM YYYY hh:mmA'` and
            // `timezone: 'Asia/Singapore'`. Per-field date format/timezone is
            // not yet plumbed through the DSL.
            selectors(".article-publish:not(span)")
        }

        dek { selectors(".content-detail__description") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors("section[data-title=\"Content\"]")

            transform("h2") { node, _ ->
                node.attr("class", "mercury-parser-keep")
                TransformResult.NoChange
            }
        }
    }
