package com.jocmp.mercury.extractors.custom.www.motorsport.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val WwwMotorsportComExtractor =
    extractor("www.motorsport.com") {
        title { attr("meta[name=\"og:title\"]", "value") }

        author { selectors(".msnt-author-toolbar a[href*=\"/info/about-us/\"]") }

        datePublished { attr("meta[name=\"datePublished\"]", "value") }

        dek { selectors("h2.text-article-description") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors(".ms-article-content")

            transform("h2") { node, _ ->
                node.attr("class", "mercury-parser-keep")
                TransformResult.NoChange
            }

            clean(
                "msnt-survey-promo",
                ".article-fullwidth-gallery_item ~ .article-fullwidth-gallery_item",
                ".ms-inarticle-widgets",
                ".relatedContent",
                ".ms-apb",
                ".ms-ap-native",
                ".outstream_partner",
            )
        }
    }
