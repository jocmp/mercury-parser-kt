package com.jocmp.mercury.extractors.custom.www.linkedin.com

import com.jocmp.mercury.extractors.extractor

val WwwLinkedinComExtractor =
    extractor("www.linkedin.com") {
        title { selectors(".article-title", "h1") }

        author {
            selector(".main-author-card h3")
            attr("meta[name=\"article:author\"]", "value")
            selector(".entity-name a[rel=author]")
        }

        datePublished {
            selectors(".base-main-card__metadata")
            attr("time[itemprop=\"datePublished\"]", "datetime")
            // timezone: 'America/Los_Angeles' (per-field timezone option not plumbed through DSL yet)
        }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream content selectors include the compound `['header figure', '.prose']`
            // which the DSL doesn't yet support; the scalar fallbacks cover the main cases.
            selectors(".article-content__body", ".prose")
            clean(".entity-image")
        }
    }
