# Custom extractor port progress

199 upstream extractors total. Authoritative list:
`ls ~/dev/jocmp/mercury-parser/src/extractors/custom | grep -v -E '^(index\.js|README\.md)$' | sort`

## Status

- **Primary extractors registered: 74**
- **Additional domains via supportedDomains: ~29** (deadspin 13, gothamist 5, www.se.pl 9, obamawhitehouse 1, pagesix 1)
- **Effective domain coverage: ~103**
- **Tests: 211 passing**

## Deferred (need DSL/cleaner extensions)

- **bialystok.se.pl, lodz.se.pl, lublin.se.pl, polityka.se.pl, portalobronny.se.pl, sport.se.pl, superbiz.se.pl, szczecin.se.pl, wroclaw.se.pl**: covered via `WwwSePlExtractor.supportedDomains` (treat as ported, no separate file)
- **blisterreview.com, factorio.com**: 2-element compound content selectors `[['a', 'b']]` not yet supported by DSL
- **genius.com**: 3-tuple attribute extraction with JSON-parsing transform — needs `Selector` post-transform fn
- **gr.euronews.com**: needs `www.euronews.com` first; will fold into its supportedDomains
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
- [x] wired.jp
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

## Todo (125 — primary domains only; some are deferred per above)

- [ ] blisterreview.com
- [ ] factorio.com
- [ ] genius.com
- [ ] gr.euronews.com
- [ ] timesofindia.indiatimes.com
- [ ] tldr.tech
- [ ] twitter.com
- [ ] uproxx.com
- [ ] wccftech.com
- [ ] weekly.ascii.jp
- [ ] wikipedia.org
- [ ] www.1pezeshk.com
- [ ] www.abendblatt.de
- [ ] www.al.com
- [ ] www.americanow.com
- [ ] www.androidauthority.com
- [ ] www.androidcentral.com
- [ ] www.aol.com
- [ ] www.apartmenttherapy.com
- [ ] www.asahi.com
- [ ] www.blick.de
- [ ] www.broadwayworld.com
- [ ] www.bustle.com
- [ ] www.buzzfeed.com
- [ ] www.cbc.ca
- [ ] www.cbssports.com
- [ ] www.channelnewsasia.com
- [ ] www.chicagotribune.com
- [ ] www.cnbc.com
- [ ] www.cnet.com
- [ ] www.dmagazine.com
- [ ] www.elecom.co.jp
- [ ] www.engadget.com
- [ ] www.eonline.com
- [ ] www.euronews.com
- [ ] www.fastcompany.com
- [ ] www.flatpanelshd.com
- [ ] www.fool.com
- [ ] www.fortinet.com
- [ ] www.futura-sciences.com
- [ ] www.gizmodo.jp
- [ ] www.gruene.de
- [ ] www.hardwarezone.com.sg
- [ ] www.heise.de
- [ ] www.huffingtonpost.com
- [ ] www.ilfattoquotidiano.it
- [ ] www.infoq.com
- [ ] www.inquisitr.com
- [ ] www.investmentexecutive.com
- [ ] www.ipa.go.jp
- [ ] www.itmedia.co.jp
- [ ] www.jnsa.org
- [ ] www.ladbible.com
- [ ] www.latimes.com
- [ ] www.lebensmittelwarnung.de
- [ ] www.lemonde.fr
- [ ] www.lifehacker.jp
- [ ] www.linkedin.com
- [ ] www.littlethings.com
- [ ] www.macrumors.com
- [ ] www.mentalfloss.com
- [ ] www.miamiherald.com
- [ ] www.moongift.jp
- [ ] www.msn.com
- [ ] www.msnbc.com
- [ ] www.n-tv.de
- [ ] www.nationalgeographic.com
- [ ] www.nbcnews.com
- [ ] www.ndtv.com
- [ ] www.newyorker.com
- [ ] www.ninefornews.nl
- [ ] www.notebookcheck.net
- [ ] www.npr.org
- [ ] www.numerama.com
- [ ] www.nydailynews.com
- [ ] www.opposingviews.com
- [ ] www.oreilly.co.jp
- [ ] www.ossnews.jp
- [ ] www.phoronix.com
- [ ] www.politico.com
- [ ] www.polygon.com
- [ ] www.popsugar.com
- [ ] www.prospectmagazine.co.uk
- [ ] www.publickey1.jp
- [ ] www.qbitai.com
- [ ] www.qdaily.com
- [ ] www.rawstory.com
- [ ] www.rbbtoday.com
- [ ] www.recode.net
- [ ] www.reddit.com
- [ ] www.refinery29.com
- [ ] www.reuters.com
- [ ] www.rollingstone.com
- [ ] www.sanwa.co.jp
- [ ] www.sbnation.com
- [ ] www.si.com
- [ ] www.slate.com
- [ ] www.spektrum.de
- [ ] www.spiegel.de
- [ ] www.tagesschau.de
- [ ] www.techpowerup.com
- [ ] www.thedrive.com
- [ ] www.thepennyhoarder.com
- [ ] www.thepoliticalinsider.com
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
