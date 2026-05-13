package com.jocmp.mercury.extractors.custom.timesofindia.indiatimes.com

import com.jocmp.mercury.extractors.extractor

val TimesofindiaIndiatimesComExtractor =
    extractor("timesofindia.indiatimes.com") {
        title { selectors("h1") }

        datePublished {
            // Upstream also specifies `format: 'MMM D, YYYY, HH:mm z'` and
            // `timezone: 'Asia/Kolkata'`. Per-field date format/timezone is not
            // yet plumbed through the DSL.
            selectors(".byline")
        }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors("div.contentwrapper:has(section)")

            defaultCleaner = false

            clean(
                "section",
                "h1",
                ".byline",
                ".img_cptn",
                ".icon_share_wrap",
                "ul[itemtype=\"https://schema.org/BreadcrumbList\"]",
            )
        }

        // Upstream also defines an `extend` block for `reporter`. The DSL
        // doesn't model extended custom fields yet.
    }
