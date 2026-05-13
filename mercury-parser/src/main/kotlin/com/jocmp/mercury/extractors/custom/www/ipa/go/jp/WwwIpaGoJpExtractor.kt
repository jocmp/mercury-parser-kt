package com.jocmp.mercury.extractors.custom.www.ipa.go.jp

import com.jocmp.mercury.extractors.extractor

val WwwIpaGoJpExtractor =
    extractor("www.ipa.go.jp") {
        title { selectors("h1") }

        datePublished {
            selectors("p.ipar_text_right")
            // format: 'YYYY年M月D日', timezone: 'Asia/Tokyo'
            // (per-field format/timezone options not plumbed through DSL yet)
        }

        content {
            selectors("#ipar_main")
            defaultCleaner = false
            clean("p.ipar_text_right")
        }
    }
