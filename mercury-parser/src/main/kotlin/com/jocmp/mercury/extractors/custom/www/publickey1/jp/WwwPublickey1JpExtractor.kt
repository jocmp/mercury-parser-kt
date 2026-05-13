package com.jocmp.mercury.extractors.custom.www.publickey1.jp

import com.jocmp.mercury.extractors.extractor

val WwwPublickey1JpExtractor =
    extractor("www.publickey1.jp") {
        title { selectors("h1") }

        author { selectors(".bloggerinchief p:first-of-type", "#subcol p:has(img)") }

        datePublished {
            selectors("div.pubdate")
            // format: 'YYYY年MM月DD日', timezone: 'Asia/Tokyo'
            // (per-field format/timezone options not plumbed through DSL yet)
        }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors("#maincol")
            defaultCleaner = false
            clean("#breadcrumbs", "div.sbm", "div.ad_footer")
        }
    }
