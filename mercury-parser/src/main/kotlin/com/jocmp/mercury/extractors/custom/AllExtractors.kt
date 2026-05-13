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
import com.jocmp.mercury.extractors.custom.mashable.com.MashableComExtractor
import com.jocmp.mercury.extractors.custom.medium.com.MediumComExtractor
import com.jocmp.mercury.extractors.custom.mobilesyrup.com.MobilesyrupComExtractor
import com.jocmp.mercury.extractors.custom.money.cnn.com.MoneyCnnComExtractor
import com.jocmp.mercury.extractors.custom.newrepublic.com.NewrepublicComExtractor
import com.jocmp.mercury.extractors.custom.news.mynavi.jp.NewsMynaviJpExtractor
import com.jocmp.mercury.extractors.custom.news.nationalgeographic.com.NewsNationalgeographicComExtractor
import com.jocmp.mercury.extractors.custom.news.pts.org.tw.NewsPtsOrgTwExtractor
import com.jocmp.mercury.extractors.custom.nymag.com.NYMagExtractor
import com.jocmp.mercury.extractors.custom.obamawhitehouse.archives.gov.ObamawhitehouseArchivesGovExtractor
import com.jocmp.mercury.extractors.custom.observer.com.ObserverComExtractor
import com.jocmp.mercury.extractors.custom.orf.at.OrfAtExtractor
import com.jocmp.mercury.extractors.custom.otrs.com.OtrsComExtractor
import com.jocmp.mercury.extractors.custom.pagesix.com.PagesixComExtractor
import com.jocmp.mercury.extractors.custom.pastebin.com.PastebinComExtractor
import com.jocmp.mercury.extractors.custom.people.com.PeopleComExtractor
import com.jocmp.mercury.extractors.custom.phpspot.org.PhpspotOrgExtractor
import com.jocmp.mercury.extractors.custom.pitchfork.com.PitchforkComExtractor
import com.jocmp.mercury.extractors.custom.polskisamorzad.se.pl.PolskisamorzadSePlExtractor
import com.jocmp.mercury.extractors.custom.qz.com.QzComExtractor
import com.jocmp.mercury.extractors.custom.scan.netsecurity.ne.jp.ScanNetsecurityNeJpExtractor
import com.jocmp.mercury.extractors.custom.sciencefly.com.ScienceflyComExtractor
import com.jocmp.mercury.extractors.custom.sect.iij.ad.jp.SectIijAdJpExtractor
import com.jocmp.mercury.extractors.custom.sg.news.yahoo.com.SgNewsYahooComExtractor
import com.jocmp.mercury.extractors.custom.superseriale.se.pl.SuperserialeSePlExtractor
import com.jocmp.mercury.extractors.custom.takagi_hiromitsu.jp.TakagiHiromitsuJpExtractor
import com.jocmp.mercury.extractors.custom.tarnkappe.info.TarnkappeInfoExtractor
import com.jocmp.mercury.extractors.custom.techcrunch.com.TechcrunchComExtractor
import com.jocmp.mercury.extractors.custom.techlog.iij.ad.jp.TechlogIijAdJpExtractor
import com.jocmp.mercury.extractors.custom.terminaltrove.com.TerminaltroveComExtractor
import com.jocmp.mercury.extractors.custom.thefederalistpapers.org.ThefederalistpapersOrgExtractor
import com.jocmp.mercury.extractors.custom.thoughtcatalog.com.ThoughtcatalogComExtractor
import com.jocmp.mercury.extractors.custom.timesofindia.indiatimes.com.TimesofindiaIndiatimesComExtractor
import com.jocmp.mercury.extractors.custom.tldr.tech.TldrTechExtractor
import com.jocmp.mercury.extractors.custom.twitter.com.TwitterComExtractor
import com.jocmp.mercury.extractors.custom.uproxx.com.UproxxComExtractor
import com.jocmp.mercury.extractors.custom.wccftech.com.WccftechComExtractor
import com.jocmp.mercury.extractors.custom.weekly.ascii.jp.WeeklyAsciiJpExtractor
import com.jocmp.mercury.extractors.custom.wikipedia.org.WikipediaOrgExtractor
import com.jocmp.mercury.extractors.custom.wired.jp.WiredJpExtractor
import com.jocmp.mercury.extractors.custom.www._1pezeshk.com.Www1pezeshkComExtractor
import com.jocmp.mercury.extractors.custom.www.abendblatt.de.WwwAbendblattDeExtractor
import com.jocmp.mercury.extractors.custom.www.al.com.WwwAlComExtractor
import com.jocmp.mercury.extractors.custom.www.americanow.com.WwwAmericanowComExtractor
import com.jocmp.mercury.extractors.custom.www.androidauthority.com.WwwAndroidauthorityComExtractor
import com.jocmp.mercury.extractors.custom.www.androidcentral.com.WwwAndroidcentralComExtractor
import com.jocmp.mercury.extractors.custom.www.aol.com.WwwAolComExtractor
import com.jocmp.mercury.extractors.custom.www.apartmenttherapy.com.ApartmentTherapyExtractor
import com.jocmp.mercury.extractors.custom.www.asahi.com.WwwAsahiComExtractor
import com.jocmp.mercury.extractors.custom.www.blick.de.WwwBlickDeExtractor
import com.jocmp.mercury.extractors.custom.www.bloomberg.com.BloombergExtractor
import com.jocmp.mercury.extractors.custom.www.broadwayworld.com.BroadwayWorldExtractor
import com.jocmp.mercury.extractors.custom.www.bustle.com.WwwBustleComExtractor
import com.jocmp.mercury.extractors.custom.www.buzzfeed.com.BuzzfeedExtractor
import com.jocmp.mercury.extractors.custom.www.cbc.ca.WwwCbcCaExtractor
import com.jocmp.mercury.extractors.custom.www.cbssports.com.WwwCbssportsComExtractor
import com.jocmp.mercury.extractors.custom.www.channelnewsasia.com.WwwChannelnewsasiaComExtractor
import com.jocmp.mercury.extractors.custom.www.chicagotribune.com.WwwChicagotribuneComExtractor
import com.jocmp.mercury.extractors.custom.www.cnbc.com.WwwCnbcComExtractor
import com.jocmp.mercury.extractors.custom.www.cnet.com.WwwCnetComExtractor
import com.jocmp.mercury.extractors.custom.www.cnn.com.WwwCnnComExtractor
import com.jocmp.mercury.extractors.custom.www.dmagazine.com.WwwDmagazineComExtractor
import com.jocmp.mercury.extractors.custom.www.elecom.co.jp.WwwElecomCoJpExtractor
import com.jocmp.mercury.extractors.custom.www.engadget.com.WwwEngadgetComExtractor
import com.jocmp.mercury.extractors.custom.www.eonline.com.WwwEonlineComExtractor
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
            MashableComExtractor,
            MediumComExtractor,
            MobilesyrupComExtractor,
            MoneyCnnComExtractor,
            NewrepublicComExtractor,
            NewsMynaviJpExtractor,
            NewsNationalgeographicComExtractor,
            NewsPtsOrgTwExtractor,
            NineToFiveGoogleComExtractor,
            NineToFiveLinuxComExtractor,
            NineToFiveMacComExtractor,
            NYMagExtractor,
            NYTimesExtractor,
            ObamawhitehouseArchivesGovExtractor,
            ObserverComExtractor,
            OrfAtExtractor,
            OtrsComExtractor,
            PagesixComExtractor,
            PastebinComExtractor,
            PeopleComExtractor,
            PhpspotOrgExtractor,
            PitchforkComExtractor,
            PolskisamorzadSePlExtractor,
            QzComExtractor,
            ScanNetsecurityNeJpExtractor,
            ScienceflyComExtractor,
            SectIijAdJpExtractor,
            SgNewsYahooComExtractor,
            SuperserialeSePlExtractor,
            TakagiHiromitsuJpExtractor,
            TarnkappeInfoExtractor,
            TechcrunchComExtractor,
            TechlogIijAdJpExtractor,
            TerminaltroveComExtractor,
            TheAtlanticExtractor,
            TheGuardianExtractor,
            ThefederalistpapersOrgExtractor,
            ThoughtcatalogComExtractor,
            TimesofindiaIndiatimesComExtractor,
            TldrTechExtractor,
            TwitterComExtractor,
            TwoFortySevenSportsComExtractor,
            UproxxComExtractor,
            WashingtonPostExtractor,
            WccftechComExtractor,
            WeeklyAsciiJpExtractor,
            WikiaExtractor,
            WikipediaOrgExtractor,
            Www1pezeshkComExtractor,
            ApartmentTherapyExtractor,
            BroadwayWorldExtractor,
            BuzzfeedExtractor,
            WwwAbendblattDeExtractor,
            WwwAlComExtractor,
            WwwAmericanowComExtractor,
            WwwAndroidauthorityComExtractor,
            WwwAndroidcentralComExtractor,
            WwwAolComExtractor,
            WwwAsahiComExtractor,
            WwwBlickDeExtractor,
            WwwBustleComExtractor,
            WwwCbcCaExtractor,
            WwwCbssportsComExtractor,
            WwwChannelnewsasiaComExtractor,
            WwwChicagotribuneComExtractor,
            WwwCnbcComExtractor,
            WwwCnetComExtractor,
            WwwDmagazineComExtractor,
            WwwElecomCoJpExtractor,
            WwwEngadgetComExtractor,
            WwwEonlineComExtractor,
            WwwSePlExtractor,
            WiredExtractor,
            WiredJpExtractor,
            WwwCnnComExtractor,
            WwwJalopnikComExtractor,
            WwwThevergeComExtractor,
        )
}
