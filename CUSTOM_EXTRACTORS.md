# Custom extractor port progress

199 upstream extractors total. Authoritative list:
`ls ~/dev/jocmp/mercury-parser/src/extractors/custom | grep -v -E '^(index\.js|README\.md)$' | sort`

## Status

- **Primary extractors registered: 114**
- **Additional domains via supportedDomains: ~31** (deadspin 13, gothamist 5, www.se.pl 9, obamawhitehouse 1, pagesix 1, buzzfeed 1, euronews 1)
- **Effective domain coverage: ~145**
- **Tests: 211 passing**

## Deferred (need DSL/cleaner extensions)

- **bialystok.se.pl, lodz.se.pl, lublin.se.pl, polityka.se.pl, portalobronny.se.pl, sport.se.pl, superbiz.se.pl, szczecin.se.pl, wroclaw.se.pl**: covered via `WwwSePlExtractor.supportedDomains` (treat as ported, no separate file)
- **blisterreview.com, factorio.com**: 2-element compound content selectors `[['a', 'b']]` not yet supported by DSL
- **genius.com**: 3-tuple attribute extraction with JSON-parsing transform — needs `Selector` post-transform fn
- **gr.euronews.com**: covered via `WwwEuronewsComExtractor.supportedDomains` (treat as ported, no separate file)
- **bookwalker.jp, japan.cnet.com, jvndb.jvn.jp, gothamist.com, www.se.pl, fortune.com, news.nationalgeographic.com, pastebin.com, phpspot.org** ported but their `date_published` `timezone`/`format` per-field options aren't plumbed through the DSL yet — see `~/.claude/projects/-Users-jocmp-dev-jocmp-ReadabilityKt/memory/mercury_deferred_fixes.md`

## Ported (44 primary)

- [x] 247sports.com
- [x] 9to5google.com
- [x] 9to5linux.com
- [x] 9to5mac.com
- [x] abcnews.go.com
- [x] arstechnica.com
- [x] balloon-juice.com
- [x] biorxiv.org
- [x] blogspot.com
- [x] bookwalker.jp
- [x] bsky.app
- [x] buzzap.jp
- [x] chicagoyimby.com
- [x] clinicaltrials.gov
- [x] deadline.com
- [x] deadspin.com (+ 13 supportedDomains: jezebel, lifehacker, kotaku, gizmodo, jalopnik, kinja, avclub, clickhole, splinternews, theonion, theroot, thetakeout, theinventory)
- [x] economictimes.indiatimes.com
- [x] epaper.zeit.de
- [x] fandom.wikia.com
- [x] fortune.com
- [x] forward.com
- [x] getnews.jp
- [x] github.com
- [x] gonintendo.com
- [x] gothamist.com (+ 5 supportedDomains: chicagoist, laist, sfist, shanghaiist, dcist)
- [x] hellogiggles.com
- [x] ici.radio-canada.ca
- [x] japan.cnet.com
- [x] japan.zdnet.com
- [x] jvndb.jvn.jp
- [x] ma.ttias.be
- [x] mashable.com
- [x] medium.com
- [x] mobilesyrup.com
- [x] money.cnn.com
- [x] newrepublic.com
- [x] news.mynavi.jp
- [x] news.nationalgeographic.com
- [x] news.pts.org.tw
- [x] nymag.com
- [x] obamawhitehouse.archives.gov (+ 1 supportedDomain: whitehouse.gov)
- [x] observer.com
- [x] orf.at
- [x] otrs.com
- [x] pagesix.com (+ 1 supportedDomain: nypost.com)
- [x] pastebin.com
- [x] people.com
- [x] phpspot.org
- [x] pitchfork.com
- [x] polskisamorzad.se.pl
- [x] qz.com
- [x] scan.netsecurity.ne.jp
- [x] sciencefly.com
- [x] sect.iij.ad.jp
- [x] sg.news.yahoo.com
- [x] superseriale.se.pl
- [x] takagi-hiromitsu.jp
- [x] tarnkappe.info
- [x] techcrunch.com
- [x] techlog.iij.ad.jp
- [x] terminaltrove.com
- [x] thefederalistpapers.org
- [x] thoughtcatalog.com
- [x] timesofindia.indiatimes.com
- [x] tldr.tech
- [x] twitter.com
- [x] uproxx.com
- [x] wccftech.com
- [x] weekly.ascii.jp
- [x] wikipedia.org
- [x] wired.jp
- [x] www.1pezeshk.com
- [x] www.abendblatt.de
- [x] www.al.com
- [x] www.americanow.com
- [x] www.androidauthority.com
- [x] www.androidcentral.com
- [x] www.aol.com
- [x] www.apartmenttherapy.com
- [x] www.asahi.com
- [x] www.blick.de
- [x] www.broadwayworld.com
- [x] www.bustle.com
- [x] www.buzzfeed.com (+ 1 supportedDomain: www.buzzfeednews.com)
- [x] www.cbc.ca
- [x] www.cbssports.com
- [x] www.channelnewsasia.com
- [x] www.chicagotribune.com
- [x] www.cnbc.com
- [x] www.cnet.com
- [x] www.dmagazine.com
- [x] www.elecom.co.jp
- [x] www.engadget.com
- [x] www.eonline.com
- [x] www.euronews.com (+ 1 supportedDomain: gr.euronews.com)
- [x] www.fastcompany.com
- [x] www.flatpanelshd.com
- [x] www.fool.com
- [x] www.fortinet.com
- [x] www.futura-sciences.com
- [x] www.gizmodo.jp
- [x] www.gruene.de
- [x] www.hardwarezone.com.sg
- [x] www.heise.de
- [x] www.bloomberg.com
- [x] www.cnn.com
- [x] www.jalopnik.com
- [x] www.nytimes.com
- [x] www.se.pl (+ 9 supportedDomains: bialystok, lodz, lublin, polityka, portalobronny, sport, superbiz, szczecin, wroclaw)
- [x] www.theatlantic.com
- [x] www.theguardian.com
- [x] www.theverge.com
- [x] www.washingtonpost.com
- [x] www.wired.com

## Todo (84 — primary domains only; some are deferred per above)

- [ ] blisterreview.com
- [ ] factorio.com
- [ ] genius.com
- [x] www.huffingtonpost.com
- [x] www.ilfattoquotidiano.it
- [x] www.infoq.com
- [x] www.inquisitr.com
- [x] www.investmentexecutive.com
- [x] www.ipa.go.jp
- [x] www.itmedia.co.jp
- [ ] www.jnsa.org  (deferred: title contains `<br>`; Jsoup `.text()` collapses to space, JS cheerio preserves `\n` — library-level parity gap)
- [x] www.ladbible.com
- [x] www.latimes.com
- [x] www.lebensmittelwarnung.de
- [x] www.lemonde.fr
- [x] www.lifehacker.jp
- [x] www.linkedin.com
- [x] www.littlethings.com
- [x] www.macrumors.com
- [x] www.mentalfloss.com
- [x] www.miamiherald.com
- [x] www.moongift.jp
- [x] www.msn.com
- [x] www.msnbc.com
- [x] www.n-tv.de
- [x] www.nationalgeographic.com
- [x] www.nbcnews.com
- [x] www.ndtv.com
- [x] www.newyorker.com
- [x] www.notebookcheck.net
- [x] www.npr.org
- [x] www.numerama.com
- [x] www.nydailynews.com
- [x] www.opposingviews.com
- [x] www.oreilly.co.jp
- [x] www.ossnews.jp
- [x] www.phoronix.com
- [x] www.politico.com
- [x] www.polygon.com
- [x] www.popsugar.com
- [x] www.prospectmagazine.co.uk
- [x] www.publickey1.jp
- [x] www.qbitai.com
- [x] www.qdaily.com
- [x] www.rawstory.com
- [x] www.rbbtoday.com
- [x] www.recode.net
- [x] www.reddit.com
- [x] www.refinery29.com
- [x] www.reuters.com
- [x] www.rollingstone.com
- [x] www.sanwa.co.jp
- [x] www.sbnation.com
- [x] www.si.com
- [x] www.slate.com
- [x] www.spektrum.de
- [x] www.spiegel.de
- [x] www.tagesschau.de
- [x] www.techpowerup.com
- [x] www.thedrive.com
- [x] www.thepennyhoarder.com
- [x] www.thepoliticalinsider.com
- [ ] www.tmz.com
- [ ] www.today.com
- [ ] www.transfermarkt.de
- [ ] www.usmagazine.com
- [ ] www.versants.com
- [ ] www.videogameschronicle.com
- [ ] www.vortez.net
- [ ] www.vox.com
- [ ] www.westernjournalism.com
- [ ] www.yahoo.com
- [ ] www.yomiuri.co.jp
- [ ] www.youtube.com

## How to continue (read this first in a fresh session)

1. **Read reference extractors** to absorb the DSL shape:
   - Simple selectors + clean: `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/custom/arstechnica/com/ArstechnicaComExtractor.kt`
   - Function transforms: `.../nymag/com/NYMagExtractor.kt`
   - Long clean list + attribute selectors: `.../www/nytimes/com/NYTimesExtractor.kt`
   - replaceWith(html) inside transform: `.../balloon_juice/com/BalloonJuiceComExtractor.kt`
   - supportedDomains pattern: `.../www/se/pl/WwwSePlExtractor.kt`

2. **DSL is at** `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/Dsl.kt`. `extractor { }`, `title { }`, `selectors(...)`, `attr(...)`, `clean(...)`, `transform(...)` are all there.

3. **Selection helpers** at `mercury-parser/src/main/kotlin/com/jocmp/mercury/dsl/Selection.kt`: `attr`, `removeAttr`, `addClass`, `parent(selector)`, `after(html)`, `replaceWith(html)`, `find`, `each`, `text`, `html`, `outerHtml`. Add more when an extractor needs something new — earlier rounds extended it for bsky/balloon-juice/ma.ttias.be.

4. **Per extractor**:
   - Read upstream JS at `~/dev/jocmp/mercury-parser/src/extractors/custom/<domain>/index.js` — source of truth
   - Port to `mercury-parser-kt/mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/custom/<host-as-package>/<Name>Extractor.kt` (dots → slashes; numeric/hyphen-leading hosts get `@file:Suppress("ktlint:standard:package-name")` at top of file)
   - Add import + entry (alphabetically) to `AllExtractors.kt`
   - Mark `[x]` in this file
   - `./gradlew test` should still pass. New snapshot parity tests get auto-discovered.

5. **Don't invent** selectors, regexes, transform logic, or comments. Every detail comes from upstream JS. Add notes when something is deferred (e.g., per-field timezone, compound selectors).

6. **Pattern spotting**:
   - `{ ...OtherExtractor, domain: 'foo' }` (spread composer) → don't port a new file; add `'foo'` to `OtherExtractor.supportedDomains`. List: `grep -l '\.\.\.' ~/dev/jocmp/mercury-parser/src/extractors/custom/*/index.js`
   - `[selector, attr]` tuple → `attr(selector, attr)` in the DSL
   - `[['a', 'b']]` compound selector → defer, drop a note, use the scalar fallback
   - `defaultCleaner: false` → `defaultCleaner = false` in the field block

7. **Run cycle** per batch: `make format && make lint && make test`. All three green. Commit with a short message naming the batch.

8. **Deferred parity fields** (don't try to "fix" parity test failures here — the test intentionally skips them; see memory file):
   - `date_published` — needs per-field timezone/format plumbed through DSL
   - `content` / `word_count` — needs cheerio↔jsoup test-side normalizer; library stays Jsoup-correct
