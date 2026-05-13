Please generate a custom extractor in mercury-parser-kt for the following link: \$ARGUMENTS.

Each step is gated on the previous one. If a step fails, stop and report — do not skip ahead and don't paper over a download/parse failure.

## 1. Fetch the page and save the fixture

- Derive `<domain>` from the URL's hostname (keep `www.` if present).
- `curl -sL --compressed -A 'Mozilla/5.0 ...' <url>` → `mercury-parser/src/test/resources/fixtures/<domain>.html` (or `<domain>/<unix-ms>.html` if multi-fixture).

## 2. Pick selectors

Priority: meta tags → semantic HTML (`h1`, `article`, `time[datetime]`) → CSS classes/ids.

- Prefer one selector; if you fall back, put meta first and structural last.
- `h2` headings inside content? Preserve them: `transform("h2") { node, _ -> node.attr("class", "mercury-parser-keep"); TransformResult.NoChange }`.
- Non-ISO dates: set `timezone = "..."` and (if needed) `format = "..."` directly on the field.
- `[['a', 'b']]` upstream → `compound("a", "b")`.
- See `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/custom/www/phoronix/com/WwwPhoronixComExtractor.kt` for a worked example.

## 3. Write the Kotlin extractor

Path: `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/custom/<host-as-package>/<Name>Extractor.kt`. Dots → slashes; hyphens become underscores in the package, and the file then needs `@file:Suppress("ktlint:standard:package-name")` at the very top.

Follow the DSL shape in `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/Dsl.kt`. No docstrings, no `// enter selectors` placeholders, no summaries.

## 4. Register

- Add an import and a list entry to `AllExtractors.kt`, alphabetized.
- Mark `[x]` in `CUSTOM_EXTRACTORS.md`.

## 5. Generate the JS reference snapshot

`TZ=UTC node scripts/dump-snapshots.mjs --only <domain>` — only works if the JS extractor already exists upstream. If it doesn't, skip and come back after step 7.

## 6. Run the suite

`make format && make lint && make test`. All three green. `SnapshotParityTest` compares against the JS dump for every field except `content` / `word_count`.

## 7. Back-port to mercury-parser (JS)

Mirror into `~/dev/jocmp/mercury-parser`:

1. **Fixtures**: copy each fixture HTML to `~/dev/jocmp/mercury-parser/src/extractors/custom/<domain>/fixtures/<basename>.html` (create the dir if needed).

2. **Extractor**: copy `.claude/templates/extractor.js.template` to `~/dev/jocmp/mercury-parser/src/extractors/custom/<domain>/index.js` and fill the placeholders from the Kotlin source. Drop any field block whose selector list would be empty; drop `timezone` / `format` lines when not set. Read the header comment in the template for the Kotlin → JS mapping cheat-sheet.

3. **Test**: copy `.claude/templates/extractor.test.js.template` to `<domain>/index.test.js`, fill the placeholders (the expected values come from running parse against the fixture, or from `Mercury.parse` once in step 6).

4. **Run the JS tests**: `cd ~/dev/jocmp/mercury-parser && npx vitest run src/extractors/custom/<domain>` (or `npx jest <domain>` on older branches). Tests must pass.

5. **Snapshot + parity round-trip**: if step 5 was deferred, now run `TZ=UTC node scripts/dump-snapshots.mjs --only <domain>` from the kt repo, then `make test`. Both repos green = done.

## 8. Report

Tell the user:

- Kotlin extractor path
- JS extractor path
- Fixtures copied
- `make test` + `npx vitest run` pass counts

Then ask if they want to see a rendered preview. If yes, copy `.claude/template.html` to a tmp file and substitute `{{TITLE}}`, `{{AUTHOR}}`, `{{CONTENT}}`, etc. with values from a one-off `Mercury.parse` call.
