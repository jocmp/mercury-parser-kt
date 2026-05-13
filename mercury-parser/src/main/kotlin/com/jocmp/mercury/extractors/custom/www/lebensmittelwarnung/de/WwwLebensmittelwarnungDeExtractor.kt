package com.jocmp.mercury.extractors.custom.www.lebensmittelwarnung.de

import com.jocmp.mercury.extractors.TransformResult
import com.jocmp.mercury.extractors.extractor

val WwwLebensmittelwarnungDeExtractor =
    extractor("www.lebensmittelwarnung.de") {
        title { selectors(".lmw-intro__heading", "title") }

        datePublished {
            attr(".lmw-intro__meta > time", "datetime")
            // format: 'DD.MM.YYYY', timezone: 'Europe/Berlin'
            // (per-field format/timezone options not plumbed through DSL yet)
        }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content {
            selectors("main")

            transform("h2") { node, _ ->
                val button = node.find("button")
                if (button.length > 0) {
                    node.find(".lmw-section__toggle-icon").remove()
                    node.elements.firstOrNull()?.text(button.text().trim())
                }
                node.attr("class", "mercury-parser-keep")
                TransformResult.NoChange
            }
            transform("ul") { node, _ ->
                node.attr("class", "mercury-parser-keep")
                TransformResult.NoChange
            }
            transform(".lmw-bodytext") { node, _ ->
                node.attr("class", "mercury-parser-keep")
                TransformResult.NoChange
            }
            transform(".lmw-description-list__item") { node, _ ->
                node.attr("class", "mercury-parser-keep")
                TransformResult.NoChange
            }
        }
    }
