Please generate a custom extractor in mercury-parser-kt for the following link: \$ARGUMENTS.

Each step below is gated on the previous step succeeding. If a step fails, stop and report — do not skip ahead and do not offer to manually paper over a download/parse failure.

## 1. Fetch the page and save the fixture

   - Derive `<domain>` from the URL's hostname (keep the `www.` prefix if present).
   - Download the article HTML to `mercury-parser/src/test/resources/fixtures/<domain>.html` (use `curl -sL --compressed -A 'Mozilla/5.0 ...' <url>`).
   - If multiple fixtures for a domain are expected, instead save under `mercury-parser/src/test/resources/fixtures/<domain>/<unix-ms>.html`.

## 2. Identify selectors

   For each field — `title`, `author`, `date_published`, `dek`, `lead_image_url`, `content`, optionally `next_page_url` — pick selectors from the fixture.

   Selector priority (most → least preferred):

   1. **Meta tags**: `attr("meta[name=\"og:title\"]", "value")` — most stable. (Mercury uses cheerio convention where `value` and `content` are interchangeable for meta tags; prefer `value`.)
   2. **Semantic HTML**: `h1`, `article`, `time[datetime]` — usually stable.
   3. **CSS classes/ids**: last resort, most brittle.

   Guidelines:

   - Prefer a single selector. If you must list fallbacks, put the meta tag first and the structural fallback last.
   - The goal is a repeatable parser — avoid brittle selectors.
   - If `h2` is used for section headings within content, add a transform to preserve them: `transform("h2") { node, _ -> node.attr("class", "mercury-parser-keep"); TransformResult.NoChange }`.
   - For `date_published` with a non-ISO source, set the upstream `timezone` / `format` strings directly: `timezone = "Asia/Tokyo"`, `format = "YYYY年M月D日"`.
   - For compound selectors (both must match, results concatenated), use `compound("a", "b")`.
   - See `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/custom/www/phoronix/com/WwwPhoronixComExtractor.kt` as a worked example with `h2` keep, format, timezone.

## 3. Write the Kotlin extractor

   Path: `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/custom/<host-as-package>/<ExtractorName>Extractor.kt` (dots → slashes; hyphens become underscores in the package name, and the file must declare `@file:Suppress("ktlint:standard:package-name")` at the very top).

   Match the DSL shape in `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/Dsl.kt` (`extractor { title { ... }; author { ... }; ... }`).

   Don't write docstrings, boilerplate `// enter selectors` comments, or summaries — just the working extractor.

## 4. Register the extractor

   - Add an import and a list entry to `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/custom/AllExtractors.kt`. Keep both lists alphabetized to match the surrounding entries.
   - Mark the domain `[x]` in `CUSTOM_EXTRACTORS.md`.

## 5. Generate a snapshot via the JS reference impl

   - `TZ=UTC node scripts/dump-snapshots.mjs --only <domain>` writes `mercury-parser/src/test/resources/snapshots/<domain>/<basename>.json`. This requires the JS extractor to already exist in `~/dev/jocmp/mercury-parser/src/extractors/custom/<domain>/` — if it doesn't, defer this step until after the JS back-port in step 7.

## 6. Run the suite

   - `make format && make lint && make test` from the repo root. All three must be green. The dynamic `SnapshotParityTest` will compare your Kotlin output against the JS snapshot for each field except `content` / `word_count`.

## 7. Back-port to mercury-parser (JS)

   After the Kotlin port passes locally, mirror the work into the JS repo at `~/dev/jocmp/mercury-parser`:

   1. **Copy the fixture(s)** into `~/dev/jocmp/mercury-parser/src/extractors/custom/<domain>/fixtures/<basename>.html`. Create the directory if it doesn't exist.

   2. **Create `~/dev/jocmp/mercury-parser/src/extractors/custom/<domain>/index.js`** by translating the Kotlin DSL back into the JS object literal. Mapping:

      - `extractor("<domain>") { ... }` → `export const <Name>Extractor = { domain: '<domain>', ... }`.
      - `selectors("a", "b")` → `selectors: ['a', 'b']`.
      - `attr("a", "v")` → `selectors: [['a', 'v']]` (a 2-tuple).
      - `attr("a", "v") { raw -> JSON.parseToJsonElement(raw)... }` → 3-tuple `['a', 'v', res => { const json = JSON.parse(res); return ...; }]`.
      - `compound("a", "b")` → nested array `[['a', 'b']]`.
      - `transform("h2", renameTo = "h3")` → `transforms: { h2: 'h3' }`.
      - `transform("sel") { node, doc -> ... }` → `transforms: { sel: ($node, $) => { ... } }` — translate `node.elements.firstOrNull()` access patterns back to cheerio's `$node.find(...)`, `$node.attr(...)`, etc.
      - `timezone = "..."` / `format = "..."` → top-level keys on `date_published`.
      - `defaultCleaner = false` → `defaultCleaner: false`.
      - `clean("x", "y")` → `clean: ['x', 'y']`.
      - `literal("foo")` → bare string value on the field (e.g. `author: 'TMZ STAFF'`).
      - `supportedDomains = listOf("a", "b")` → `supportedDomains: ['a', 'b']`.

      Preserve the field order from the JS template (`title`, `author`, `date_published`, `dek`, `lead_image_url`, `content`, then `next_page_url` / `excerpt` if used). Don't include `// enter selectors` placeholders.

   3. **Create a basic `index.test.js`** in the same directory that mirrors the style of an existing one (e.g. `www.phoronix.com/index.test.js`): import the extractor, load each fixture via `fs.readFileSync`, call `Mercury.parse`, assert on `title`, `author`, `date_published`, `lead_image_url`, and a content snippet.

   4. **Register the JS extractor** by adding it to `~/dev/jocmp/mercury-parser/src/extractors/custom/index.js` (this file is auto-generated; run whatever the upstream uses — usually `npm run generate-custom-parser` is interactive, but for already-created directories the import gets picked up automatically by Vitest's discovery; if not, add it manually).

   5. **Run the JS tests** in the mercury-parser directory: `npx vitest run src/extractors/custom/<domain>` (or `npx jest <domain>` on older branches). Tests must pass.

   6. **Re-run the Kotlin parity test** from mercury-parser-kt: if you skipped step 5 earlier because the JS extractor didn't exist yet, now run `TZ=UTC node scripts/dump-snapshots.mjs --only <domain>` to generate the snapshot, then `make test` to verify parity. Both repos green = done.

## 8. Report

   Show the user:

   - The Kotlin extractor file path
   - The JS extractor file path
   - The list of fixtures copied
   - `make test` and `npx vitest run` summaries (passing counts)

   Then ask if they want to see a preview of the output. If yes, create a tmp file with the contents of `.claude/template.html` and substitute `{{TITLE}}`, `{{AUTHOR}}`, `{{CONTENT}}`, etc. with the values from a one-off `Mercury.parse` call.
