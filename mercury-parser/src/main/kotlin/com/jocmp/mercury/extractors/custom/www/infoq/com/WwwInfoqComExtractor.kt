package com.jocmp.mercury.extractors.custom.www.infoq.com

import com.jocmp.mercury.extractors.extractor

val WwwInfoqComExtractor =
    extractor("www.infoq.com") {
        title { selectors("h1.heading") }

        author { selectors("div.widget.article__authors") }

        datePublished {
            selectors(".article__readTime.date")
            // format: 'YYYY年M月D日', timezone: 'Asia/Tokyo'
            // (per-field format/timezone options not plumbed through DSL yet)
        }

        dek { attr("meta[name=\"og:description\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors("div.article__data")
            defaultCleaner = false
        }
    }
