package com.jocmp.mercury.extractors.custom

import com.jocmp.mercury.extractors.CustomExtractor
import com.jocmp.mercury.extractors.custom.abcnews.go.com.AbcnewsGoComExtractor
import com.jocmp.mercury.extractors.custom.arstechnica.com.ArstechnicaComExtractor
import com.jocmp.mercury.extractors.custom.money.cnn.com.MoneyCnnComExtractor
import com.jocmp.mercury.extractors.custom.nymag.com.NYMagExtractor
import com.jocmp.mercury.extractors.custom.wired.jp.WiredJpExtractor
import com.jocmp.mercury.extractors.custom.www.bloomberg.com.BloombergExtractor
import com.jocmp.mercury.extractors.custom.www.cnn.com.WwwCnnComExtractor
import com.jocmp.mercury.extractors.custom.www.nytimes.com.NYTimesExtractor
import com.jocmp.mercury.extractors.custom.www.theatlantic.com.TheAtlanticExtractor
import com.jocmp.mercury.extractors.custom.www.theguardian.com.TheGuardianExtractor
import com.jocmp.mercury.extractors.custom.www.theverge.com.WwwThevergeComExtractor
import com.jocmp.mercury.extractors.custom.www.washingtonpost.com.WashingtonPostExtractor
import com.jocmp.mercury.extractors.custom.www.wired.com.WiredExtractor

/**
 * Central listing of every custom extractor. Each ported site adds itself
 * to [all] and the registry pulls them in on first access. Keeps registration
 * deterministic (no SPI / classpath scanning) and trivial to grep.
 */
object AllExtractors {
    val all: List<CustomExtractor> =
        listOf(
            AbcnewsGoComExtractor,
            ArstechnicaComExtractor,
            BloombergExtractor,
            MoneyCnnComExtractor,
            NYMagExtractor,
            NYTimesExtractor,
            TheAtlanticExtractor,
            TheGuardianExtractor,
            WashingtonPostExtractor,
            WiredExtractor,
            WiredJpExtractor,
            WwwCnnComExtractor,
            WwwThevergeComExtractor,
        )
}
