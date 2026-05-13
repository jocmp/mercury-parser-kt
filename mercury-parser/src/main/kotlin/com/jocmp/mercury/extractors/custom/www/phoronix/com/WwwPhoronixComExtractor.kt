package com.jocmp.mercury.extractors.custom.www.phoronix.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val WwwPhoronixComExtractor =
    extractor("www.phoronix.com") {
        title { selectors("article h1", "article header") }

        author { selectors(".author a:first-child") }

        datePublished {
            selectors(".author")
            // format: 'D MMMM YYYY at hh:mm', timezone: 'America/New_York'
            // (per-field format/timezone options not plumbed through DSL yet)
        }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors(".content", "article")
            defaultCleaner = false

            transform("h2") { node, _ ->
                node.attr("class", "mercury-parser-keep")
                TransformResult.NoChange
            }
        }
    }
