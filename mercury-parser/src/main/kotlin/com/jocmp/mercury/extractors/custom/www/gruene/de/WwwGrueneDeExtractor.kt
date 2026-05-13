package com.jocmp.mercury.extractors.custom.www.gruene.de

import com.jocmp.mercury.extractors.extractor

val WwwGrueneDeExtractor =
    extractor("www.gruene.de") {
        title { selectors("header h1") }

        leadImageUrl { attr("meta[property=\"og:image\"]", "content") }

        content {
            // Upstream's only selector is a 4-element compound array
            // ['section header', 'section h2', 'section p', 'section ol'].
            // Compound selectors are not yet supported by the Kotlin DSL —
            // falling through to the scalar `section` (commented-out alternative
            // in the upstream source).
            selectors("section")

            clean("figcaption", "p[class]")
        }
    }
