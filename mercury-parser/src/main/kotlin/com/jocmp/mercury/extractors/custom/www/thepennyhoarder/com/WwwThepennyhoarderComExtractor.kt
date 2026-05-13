package com.jocmp.mercury.extractors.custom.www.thepennyhoarder.com

import com.jocmp.mercury.extractors.extractor

val WwwThepennyhoarderComExtractor =
    extractor("www.thepennyhoarder.com") {
        title { attr("meta[name=\"dcterms.title\"]", "value") }

        author { attr("link[rel=\"author\"]", "title") }

        datePublished { attr("meta[name=\"article:published_time\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream includes compound selector `['.post-img', '.post-text']`
            // which the DSL doesn't yet support; scalar fallbacks cover the rest.
            selectors(".post-text", ".single-post-content-inner")
        }
    }
