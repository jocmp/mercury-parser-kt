package com.jocmp.mercury.extractors.custom.www.animenewsnetwork.com

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val WwwAnimenewsnetworkComExtractor =
    extractor("www.animenewsnetwork.com") {
        title { attr("meta[name=\"og:title\"]", "value") }

        datePublished { attr("small time", "datetime") }

        dek { attr("meta[name=\"description\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors(".KonaBody")

            transform("img") { node, _ ->
                val dataSrc = node.attr("data-src")
                if (dataSrc != null) {
                    var src = dataSrc
                    if (dataSrc.startsWith("/")) {
                        src = "https://www.animenewsnetwork.com$dataSrc"
                    }
                    node.attr("src", src)
                    node.removeAttr("data-src")
                }
                TransformResult.NoChange
            }

            clean(".intro", "instaread-player")
        }
    }
