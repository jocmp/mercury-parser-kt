package com.jocmp.mercury.extractors.custom.metsul.com

import com.jocmp.mercury.extractors.extractor

val MetsulComExtractor =
    extractor("metsul.com") {
        title { attr("meta[name=\"og:title\"]", "value") }

        author { attr("meta[name=\"author\"]", "value") }

        datePublished { attr("meta[name=\"article:published_time\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors(".post-content")

            clean(".metsul-leia-tambem", ".metsu-entity-placement")
        }
    }
