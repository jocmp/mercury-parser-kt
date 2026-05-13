package com.jocmp.mercury.extractors.custom.japan.cnet.com

import com.jocmp.mercury.extractors.extractor

val JapanCnetComExtractor =
    extractor("japan.cnet.com") {
        title { selectors(".leaf-headline-ttl") }

        author { selectors(".writer") }

        datePublished {
            selectors(".date")
            // format: 'YYYY年M月D日 HH時mm分', timezone: 'Asia/Tokyo'
            // (per-field format/timezone options not plumbed through DSL yet)
        }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content { selectors("div.article_body") }
    }
