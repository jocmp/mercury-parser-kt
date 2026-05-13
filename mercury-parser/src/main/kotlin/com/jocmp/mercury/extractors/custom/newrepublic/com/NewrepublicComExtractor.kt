package com.jocmp.mercury.extractors.custom.newrepublic.com

import com.jocmp.mercury.extractors.extractor

val NewrepublicComExtractor =
    extractor("newrepublic.com") {
        title { selectors("h1.article-headline") }

        author { selectors("span.AuthorList") }

        datePublished { attr("meta[name=\"article:published_time\"]", "value") }

        dek { selectors("h2.article-subhead") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream wraps the selector in a 1-element compound array
            // [['div.article-body']]. The DSL doesn't carry compound selectors
            // yet; the scalar form is equivalent here since the array is length 1.
            selectors("div.article-body")

            clean("aside")
        }
    }
