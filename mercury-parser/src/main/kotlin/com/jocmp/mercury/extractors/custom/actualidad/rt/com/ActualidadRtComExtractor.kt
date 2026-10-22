package com.jocmp.mercury.extractors.custom.actualidad.rt.com

import com.jocmp.mercury.extractors.extractor

val ActualidadRtComExtractor =
    extractor("actualidad.rt.com") {
        title { attr("meta[name=\"og:title\"]", "value") }

        author { attr("meta[name=\"article:author\"]", "value") }

        datePublished { attr("meta[name=\"mediator_published_time\"]", "value") }

        dek { attr("meta[name=\"og:description\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors(".ArticleView-text")

            clean(".ReadMore-root", "source")
        }
    }
