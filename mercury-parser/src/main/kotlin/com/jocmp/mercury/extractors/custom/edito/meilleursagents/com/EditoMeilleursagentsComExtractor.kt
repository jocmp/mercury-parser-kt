package com.jocmp.mercury.extractors.custom.edito.meilleursagents.com

import com.jocmp.mercury.extractors.extractor

val EditoMeilleursagentsComExtractor =
    extractor("edito.meilleursagents.com") {
        title { attr("meta[name=\"og:title\"]", "value") }

        author { selectors(".field--name-uid .username") }

        datePublished { attr(".field--name-published-at time", "datetime") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors(".main-contenu")

            clean(
                ".visually-hidden",
                ".field--name-field-sommaire",
                ".paragraph--type--texte-cta",
            )
        }
    }
