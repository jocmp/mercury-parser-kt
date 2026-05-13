package com.jocmp.mercury.extractors.custom.www.buzzfeed.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val BuzzfeedExtractor =
    extractor("www.buzzfeed.com") {
        supportedDomains = listOf("www.buzzfeednews.com")

        title { selectors("h1.embed-headline-title") }

        author {
            selector("a[data-action=\"user/username\"]")
            selector("byline__author")
            attr("meta[name=\"author\"]", "value")
        }

        datePublished { attr("time[datetime]", "datetime") }

        dek { selectors(".embed-headline-description") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream first selector is a 2-element compound array
            // ['div[class^="featureimage_featureImageWrapper"]', '.js-subbuzz-wrapper'].
            // Compound selectors are not yet supported by the Kotlin DSL — falling
            // through to the scalar `.js-subbuzz-wrapper`.
            selectors(".js-subbuzz-wrapper")

            defaultCleaner = false

            transform("h2", renameTo = "b")

            transform("div.longform_custom_header_media") { node, _ ->
                val el = node.elements.firstOrNull() ?: return@transform TransformResult.NoChange
                if (el.selectFirst("img") != null && el.selectFirst(".longform_header_image_source") != null) {
                    TransformResult.Rename("figure")
                } else {
                    TransformResult.NoChange
                }
            }
            transform(
                "figure.longform_custom_header_media .longform_header_image_source",
                renameTo = "figcaption",
            )

            clean(
                ".instapaper_ignore",
                ".suplist_list_hide .buzz_superlist_item .buzz_superlist_number_inline",
                ".share-box",
                ".print",
                ".js-inline-share-bar",
                ".js-ad-placement",
            )
        }
    }
