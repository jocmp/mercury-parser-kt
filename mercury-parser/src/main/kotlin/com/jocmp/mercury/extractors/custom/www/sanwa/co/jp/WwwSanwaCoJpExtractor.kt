package com.jocmp.mercury.extractors.custom.www.sanwa.co.jp

import com.jocmp.mercury.extractors.extractor

val WwwSanwaCoJpExtractor =
    extractor("www.sanwa.co.jp") {
        title { selectors("#newsContent h1") }

        datePublished {
            selectors("dl.date")
            // format: 'YYYY.M.D', timezone: 'Asia/Tokyo'
            // (per-field format/timezone options not plumbed through DSL yet)
        }

        dek { attr("meta[name=\"og:description\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors("#newsContent")
            defaultCleaner = false
            clean("#smartphone", "div.sns_box", "div.contentFoot")
        }
    }
