package com.jocmp.mercury.extractors.custom.www.journaldelagence.com

import com.jocmp.mercury.extractors.extractor

val WwwJournaldelagenceComExtractor =
    extractor("www.journaldelagence.com") {
        title { selectors("h1.full-article-ttl") }

        datePublished { attr("meta[name=\"article:published_time\"]", "value") }

        leadImageUrl { attr("meta[name=\"og:image\"]", "value") }

        content { selectors("#js-readspeaker") }
    }
