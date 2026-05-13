package com.jocmp.mercury.extractors.custom

import com.jocmp.mercury.extractors.CustomExtractor
import com.jocmp.mercury.extractors.custom.arstechnica.com.ArstechnicaComExtractor
import com.jocmp.mercury.extractors.custom.nymag.com.NYMagExtractor
import com.jocmp.mercury.extractors.custom.www.nytimes.com.NYTimesExtractor

/**
 * Central listing of every custom extractor. Each ported site adds itself
 * to [all] and the registry pulls them in on first access. Keeps registration
 * deterministic (no SPI / classpath scanning) and trivial to grep.
 */
object AllExtractors {
    val all: List<CustomExtractor> =
        listOf(
            ArstechnicaComExtractor,
            NYMagExtractor,
            NYTimesExtractor,
        )
}
