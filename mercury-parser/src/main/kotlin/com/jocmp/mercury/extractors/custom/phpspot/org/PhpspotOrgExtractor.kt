package com.jocmp.mercury.extractors.custom.phpspot.org

import com.jocmp.mercury.extractors.extractor

val PhpspotOrgExtractor =
    extractor("phpspot.org") {
        title { selectors("h3.hl") }

        datePublished {
            // Upstream also specifies `format: 'YYYY年M月D日'` and
            // `timezone: 'Asia/Tokyo'`. Per-field date format/timezone is not
            // yet plumbed through the DSL (see mercury-deferred-fixes memory).
            selectors("h4.hl")
        }

        content {
            selectors("div.entrybody")

            defaultCleaner = false
        }
    }
