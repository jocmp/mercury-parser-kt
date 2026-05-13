package com.jocmp.mercury.extractors.custom.jvndb.jvn.jp

import com.jocmp.mercury.extractors.extractor

val JvndbJvnJpExtractor =
    extractor("jvndb.jvn.jp") {
        title { selectors("title") }

        datePublished {
            selectors("div.modifytxt:nth-child(2)")
            // format: 'YYYY/M/D', timezone: 'Asia/Tokyo'
            // (per-field format/timezone options not plumbed through DSL yet)
        }

        content {
            selectors("#news-list")
            defaultCleaner = false
        }
    }
