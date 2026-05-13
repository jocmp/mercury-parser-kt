package com.jocmp.mercury.extractors.custom.www.miamiherald.com

import com.jocmp.mercury.extractors.extractor

val WwwMiamiheraldComExtractor =
    extractor("www.miamiherald.com") {
        title { selectors("h1.title") }

        datePublished {
            selectors("p.published-date")
            // timezone: 'America/New_York' (per-field timezone option not plumbed through DSL yet)
        }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors("div.dateline-storybody")
        }
    }
