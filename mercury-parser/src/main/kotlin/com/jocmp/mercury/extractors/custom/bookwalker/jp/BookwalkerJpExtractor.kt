package com.jocmp.mercury.extractors.custom.bookwalker.jp

import com.jocmp.mercury.extractors.extractor

val BookwalkerJpExtractor =
    extractor("bookwalker.jp") {
        title { selectors("h1.p-main__title", "h1.main-heading") }

        author { selectors("div.p-author__list", "div.authors") }

        datePublished {
            selectors(
                "dl.p-information__data dd:nth-of-type(7)",
                ".work-info .work-detail:first-of-type .work-detail-contents:last-of-type",
            )
            // Upstream specifies timezone: 'Asia/Tokyo'. Per-field timezone is not
            // yet plumbed through the DSL → cleanDatePublished, so this date may
            // parse with a different offset than JS Mercury would produce.
        }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream's second selector is a 2-element compound array
            // ['div.main-info', 'div.main-cover-inner'] (both must match,
            // results concatenated). Compound selectors are not yet supported
            // by the Kotlin DSL — only the first scalar selector is honored.
            selectors("div.p-main__information")

            defaultCleaner = false

            clean(
                "span.label.label--trial",
                "dt.info-head.info-head--coin",
                "dd.info-contents.info-contents--coin",
                "div.info-notice.fn-toggleClass",
            )
        }
    }
