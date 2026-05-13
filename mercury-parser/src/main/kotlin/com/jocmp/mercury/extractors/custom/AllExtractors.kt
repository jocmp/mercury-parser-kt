package com.jocmp.mercury.extractors.custom

import com.jocmp.mercury.extractors.CustomExtractor
import com.jocmp.mercury.extractors.custom._247sports.com.TwoFortySevenSportsComExtractor
import com.jocmp.mercury.extractors.custom._9to5google.com.NineToFiveGoogleComExtractor
import com.jocmp.mercury.extractors.custom._9to5linux.com.NineToFiveLinuxComExtractor
import com.jocmp.mercury.extractors.custom._9to5mac.com.NineToFiveMacComExtractor
import com.jocmp.mercury.extractors.custom.abcnews.go.com.AbcnewsGoComExtractor
import com.jocmp.mercury.extractors.custom.arstechnica.com.ArstechnicaComExtractor
import com.jocmp.mercury.extractors.custom.balloon_juice.com.BalloonJuiceComExtractor
import com.jocmp.mercury.extractors.custom.biorxiv.org.BiorxivOrgExtractor
import com.jocmp.mercury.extractors.custom.blogspot.com.BloggerExtractor
import com.jocmp.mercury.extractors.custom.bookwalker.jp.BookwalkerJpExtractor
import com.jocmp.mercury.extractors.custom.bsky.app.BskyAppExtractor
import com.jocmp.mercury.extractors.custom.buzzap.jp.BuzzapJpExtractor
import com.jocmp.mercury.extractors.custom.chicagoyimby.com.ChicagoyimbyComExtractor
import com.jocmp.mercury.extractors.custom.clinicaltrials.gov.ClinicaltrialsGovExtractor
import com.jocmp.mercury.extractors.custom.deadline.com.DeadlineComExtractor
import com.jocmp.mercury.extractors.custom.deadspin.com.DeadspinExtractor
import com.jocmp.mercury.extractors.custom.economictimes.indiatimes.com.EconomictimesIndiatimesComExtractor
import com.jocmp.mercury.extractors.custom.epaper.zeit.de.EpaperZeitDeExtractor
import com.jocmp.mercury.extractors.custom.fandom.wikia.com.WikiaExtractor
import com.jocmp.mercury.extractors.custom.fortune.com.FortuneComExtractor
import com.jocmp.mercury.extractors.custom.forward.com.ForwardComExtractor
import com.jocmp.mercury.extractors.custom.getnews.jp.GetnewsJpExtractor
import com.jocmp.mercury.extractors.custom.github.com.GithubComExtractor
import com.jocmp.mercury.extractors.custom.gonintendo.com.GonintendoComExtractor
import com.jocmp.mercury.extractors.custom.gothamist.com.GothamistComExtractor
import com.jocmp.mercury.extractors.custom.hellogiggles.com.HellogigglesComExtractor
import com.jocmp.mercury.extractors.custom.ici.radio_canada.ca.IciRadioCanadaCaExtractor
import com.jocmp.mercury.extractors.custom.japan.cnet.com.JapanCnetComExtractor
import com.jocmp.mercury.extractors.custom.japan.zdnet.com.JapanZdnetComExtractor
import com.jocmp.mercury.extractors.custom.jvndb.jvn.jp.JvndbJvnJpExtractor
import com.jocmp.mercury.extractors.custom.ma.ttias.be.MaTtiasBeExtractor
import com.jocmp.mercury.extractors.custom.money.cnn.com.MoneyCnnComExtractor
import com.jocmp.mercury.extractors.custom.nymag.com.NYMagExtractor
import com.jocmp.mercury.extractors.custom.wired.jp.WiredJpExtractor
import com.jocmp.mercury.extractors.custom.www.bloomberg.com.BloombergExtractor
import com.jocmp.mercury.extractors.custom.www.cnn.com.WwwCnnComExtractor
import com.jocmp.mercury.extractors.custom.www.jalopnik.com.WwwJalopnikComExtractor
import com.jocmp.mercury.extractors.custom.www.nytimes.com.NYTimesExtractor
import com.jocmp.mercury.extractors.custom.www.se.pl.WwwSePlExtractor
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
            BalloonJuiceComExtractor,
            BiorxivOrgExtractor,
            BloggerExtractor,
            BloombergExtractor,
            BookwalkerJpExtractor,
            BskyAppExtractor,
            BuzzapJpExtractor,
            ChicagoyimbyComExtractor,
            ClinicaltrialsGovExtractor,
            DeadlineComExtractor,
            DeadspinExtractor,
            EconomictimesIndiatimesComExtractor,
            EpaperZeitDeExtractor,
            FortuneComExtractor,
            ForwardComExtractor,
            GetnewsJpExtractor,
            GithubComExtractor,
            GonintendoComExtractor,
            GothamistComExtractor,
            HellogigglesComExtractor,
            IciRadioCanadaCaExtractor,
            JapanCnetComExtractor,
            JapanZdnetComExtractor,
            JvndbJvnJpExtractor,
            MaTtiasBeExtractor,
            MoneyCnnComExtractor,
            NineToFiveGoogleComExtractor,
            NineToFiveLinuxComExtractor,
            NineToFiveMacComExtractor,
            NYMagExtractor,
            NYTimesExtractor,
            TheAtlanticExtractor,
            TheGuardianExtractor,
            TwoFortySevenSportsComExtractor,
            WashingtonPostExtractor,
            WikiaExtractor,
            WwwSePlExtractor,
            WiredExtractor,
            WiredJpExtractor,
            WwwCnnComExtractor,
            WwwJalopnikComExtractor,
            WwwThevergeComExtractor,
        )
}
