@file:Suppress("ktlint:standard:package-name")

package com.jocmp.mercury.extractors.custom.actualites.logic_immo.com

import com.jocmp.mercury.extractors.extractor

val ActualitesLogicImmoComExtractor =
    extractor("actualites.logic-immo.com") {
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
