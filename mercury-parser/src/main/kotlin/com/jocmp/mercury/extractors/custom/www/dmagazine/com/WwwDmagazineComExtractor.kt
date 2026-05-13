package com.jocmp.mercury.extractors.custom.www.dmagazine.com

import com.jocmp.mercury.extractors.extractor

val WwwDmagazineComExtractor =
    extractor("www.dmagazine.com") {
        title { selectors("h1.story__title") }

        author { selectors(".story__info .story__info__item:first-child") }

        datePublished {
            // Upstream also specifies `format: 'MMMM D, YYYY h:mm a'` and
            // `timezone: 'America/Chicago'`. Per-field date format/timezone is
            // not yet plumbed through the DSL.
            selectors(".story__info")
        }

        dek { selectors(".story__subhead") }

        leadImageUrl { attr("article figure a:first-child", "href") }

        content {
            selectors(".story__content")
        }
    }
