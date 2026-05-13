package com.jocmp.mercury.extractors.custom.news.nationalgeographic.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val NewsNationalgeographicComExtractor =
    extractor("news.nationalgeographic.com") {
        title { selectors("h1", "h1.main-title") }

        author { selectors(".byline-component__contributors b span") }

        datePublished {
            // Upstream also specifies `format: 'ddd MMM D HH:mm:ss zz YYYY'`
            // and `timezone: 'EST'`. Per-field date format/timezone is not yet
            // plumbed through the DSL (see mercury-deferred-fixes memory).
            attr("meta[name=\"article:published_time\"]", "value")
        }

        dek { selectors(".article__deck") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            // Upstream first selector is a 2-element compound array
            // ['.parsys.content', '.__image-lead__']. Compound selectors are not
            // yet supported by the Kotlin DSL — falling through to the scalar `.content`.
            selectors(".content")

            transform(".parsys.content") { node, _ ->
                val el = node.elements.firstOrNull() ?: return@transform TransformResult.NoChange
                val imgSrc = el.selectFirst(".image.parbase.section .picturefill")?.attr("data-platform-src")
                if (!imgSrc.isNullOrEmpty()) {
                    el.prepend("<img class=\"__image-lead__\" src=\"$imgSrc\"/>")
                }
                TransformResult.NoChange
            }

            clean(".pull-quote.pull-quote--large")
        }
    }
