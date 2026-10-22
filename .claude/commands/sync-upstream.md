Port changes from upstream mercury-parser (JS) into mercury-parser-kt. Range: \$ARGUMENTS.

mercury-parser is the source of truth. New extractors are created there first (its own `create-parser` command); this repo only follows. Never write JS or touch the upstream repo from here.

Each step is gated on the previous one. If a step fails, stop and report. Do not skip ahead or paper over a failure.

## 1. Resolve the range

- Upstream checkout: `${MERCURY_PARSER_DIR:-../mercury-parser}`. Run `git fetch origin` there. `dump-snapshots.mjs` reads its working tree, so it must match `origin/main`. If the checkout is on another branch or dirty, don't touch it: `git -C <upstream> worktree add --detach <tmp> origin/main`, then `npm ci && npm run build` in `<tmp>` and export `MERCURY_PARSER_DIR=<tmp>`. Remove the worktree when done.
- If `$ARGUMENTS` is a range (`<from>..<to>`), use it. Otherwise use `$(cat UPSTREAM_REF)..origin/main`.
- If the range is empty, report "already in sync" and stop.

## 2. Classify the diff

`git -C <upstream> log --oneline <range> -- src/` and `git -C <upstream> diff --stat <range> -- src/`. Sort every change into one bucket:

- **New extractor**: a new `src/extractors/custom/<domain>/` directory.
- **Changed extractor**: edits to an existing `src/extractors/custom/<domain>/index.js` or its fixtures in `fixtures/<domain>/`.
- **Spread composer**: a new `{ ...OtherExtractor, domain: 'foo' }` -> add `'foo'` to that extractor's `supportedDomains`, no new file.
- **Generic**: anything outside `src/extractors/custom/<domain>/` (cleaners, scoring, utils, resource, generic extractors).
- **Ignore**: test-only edits (`index.test.js`), `src/extractors/custom/index.js` registration, formatting, comments, browser-compat rewrites with no behavior change.

Print the buckets before porting anything.

## 3. Port extractors

For each new or changed extractor, follow the rules in `CUSTOM_EXTRACTORS.md` ("Per extractor", "Don't invent", "Pattern spotting"). Upstream JS is the source of truth for selectors, transforms, and clean lists.

- **Fixtures**: upstream keeps them at the repo root in `fixtures/<domain>/<basename>.html`. Copy each to `mercury-parser/src/test/resources/fixtures/<domain>/<basename>.html`, keeping the basename so the snapshot lines up.
- **Extractor**: `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/custom/<host-as-package>/<Name>Extractor.kt`. Dots -> slashes; hyphens become underscores in the package, and the file then needs `@file:Suppress("ktlint:standard:package-name")` at the very top. Follow `mercury-parser/src/main/kotlin/com/jocmp/mercury/extractors/Dsl.kt`. No docstrings or comments.
- **JS -> Kotlin mapping**:
  - `[selector, attr]` -> `attr(selector, attr)`
  - `[['a', 'b']]` -> `compound("a", "b")`
  - `timezone` / `format` on a field -> `timezone = "..."` / `format = "..."` on that field
  - `defaultCleaner: false` -> `defaultCleaner = false`
  - `h2: node => node.attr('class', 'mercury-parser-keep')` -> `transform("h2") { node, _ -> node.attr("class", "mercury-parser-keep"); TransformResult.NoChange }`
  - Worked example: `.../extractors/custom/www/phoronix/com/WwwPhoronixComExtractor.kt` against upstream `src/extractors/custom/www.phoronix.com/index.js`
- **Register**: import + alphabetized entry in `AllExtractors.kt`; mark `[x]` (or add the row) in `CUSTOM_EXTRACTORS.md` and update the counts in its Status section.

## 4. Snapshots and suite

- From upstream: `npm ci && npm run build` if `dist/` is missing or stale.
- `TZ=UTC node scripts/dump-snapshots.mjs` with no `--only`, so every snapshot reflects the new upstream (set `MERCURY_PARSER_DIR` if upstream is not at `../mercury-parser`).
- `git status mercury-parser/src/test/resources/snapshots`:
  - A new snapshot dir for a domain outside the range means an upstream extractor kt never ported. Port it the same way (step 3) and call it out as a backfill in the report.
  - A modified snapshot means upstream behavior changed for that site. Check which fields moved; changes limited to `content` / `word_count` are skipped by parity, anything else needs a matching Kotlin change.
- `make format && make lint && make test`. All three green. `SnapshotParityTest` compares every field except `content` / `word_count`.

## 5. Generic changes

Do not port these silently. For each generic commit, read the upstream diff and the matching Kotlin code, then either:

- port it when the mapping is direct and covered by an existing test, or
- leave it and list it in the report with the Kotlin file it would touch and why it was not ported.

Parity skips `content` / `word_count`, which is where generic changes usually show up, so say which ones lack test coverage.

## 6. Advance the ref and commit

- Write the resolved `<to>` SHA (full, from `git -C <upstream> rev-parse`) to `UPSTREAM_REF`. Only advance it past commits that were ported or deliberately ignored; if a generic change is left pending, still advance but list it in the report.
- Branch `jc/sync-upstream-<short-to-sha>`, one commit per extractor batch plus one for `UPSTREAM_REF`.

## 7. Report

- Range synced
- Extractors ported (Kotlin paths) and fixtures copied
- Generic changes: ported / pending, with reasons
- `make test` pass count

Then ask whether to push and open a PR.
