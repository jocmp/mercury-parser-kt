#!/usr/bin/env node
/**
 * Dump Mercury.parse JSON output for every fixture in the upstream
 * mercury-parser repo, writing one snapshot per (domain, fixture-file)
 * into the Kotlin test resources tree:
 *
 *   mercury-parser-kt/mercury-parser/src/test/resources/snapshots/
 *     <domain>/
 *       <fixture-basename>.json
 *
 * Usage:
 *   MERCURY_PARSER_DIR=~/dev/jocmp/mercury-parser \
 *     node scripts/dump-snapshots.mjs [--only <domain>]
 *
 * The script is run inside the mercury-parser repo so Mercury can resolve
 * its own dependencies; we cd into MERCURY_PARSER_DIR and import its built
 * bundle.
 */
import fs from 'node:fs';
import path from 'node:path';
import process from 'node:process';
import { fileURLToPath } from 'node:url';

const HERE = path.dirname(fileURLToPath(import.meta.url));
const REPO = path.resolve(HERE, '..');
const SNAPSHOTS_DIR = path.join(
  REPO,
  'mercury-parser',
  'src',
  'test',
  'resources',
  'snapshots',
);

const MERCURY_DIR =
  process.env.MERCURY_PARSER_DIR ||
  path.resolve(HERE, '..', '..', 'mercury-parser');

if (!fs.existsSync(MERCURY_DIR)) {
  console.error(`mercury-parser dir not found: ${MERCURY_DIR}`);
  process.exit(2);
}

const args = process.argv.slice(2);
const only = (() => {
  const idx = args.indexOf('--only');
  return idx >= 0 ? args[idx + 1] : null;
})();

// Mercury's `dist/mercury.esm.js` is the browser bundle (requires window).
// Use the CJS Node build instead.
const mercuryEntry = path.join(MERCURY_DIR, 'dist', 'mercury.js');
if (!fs.existsSync(mercuryEntry)) {
  console.error(
    `Mercury bundle missing at ${mercuryEntry}. Run \`yarn build\` in mercury-parser first.`,
  );
  process.exit(3);
}

// Mercury caches per-instance state between calls (notably some extractor
// lookups). Load it via createRequire so we can drop the cached module entry
// between fixtures to keep snapshots deterministic.
const { createRequire } = await import('node:module');
const requireCjs = createRequire(import.meta.url);
function loadMercury() {
  const cacheKey = requireCjs.resolve(mercuryEntry);
  delete requireCjs.cache[cacheKey];
  const mod = requireCjs(mercuryEntry);
  return mod.default || mod;
}

const fixturesRoot = path.join(MERCURY_DIR, 'fixtures');
if (!fs.existsSync(fixturesRoot)) {
  console.error(`Mercury fixtures dir missing: ${fixturesRoot}`);
  process.exit(4);
}

fs.mkdirSync(SNAPSHOTS_DIR, { recursive: true });

const entries = fs.readdirSync(fixturesRoot, { withFileTypes: true });
let count = 0;
let errors = 0;

for (const entry of entries) {
  if (only && !entry.name.startsWith(only)) continue;

  if (entry.isDirectory()) {
    const domain = entry.name;
    const domainDir = path.join(fixturesRoot, domain);
    const files = fs.readdirSync(domainDir).filter(f => f.endsWith('.html'));
    for (const file of files) {
      const result = await runOne(
        domain,
        path.join(domainDir, file),
        path.basename(file, '.html'),
      );
      if (result === 'error') errors += 1;
      else if (result === 'ok') count += 1;
    }
    continue;
  }

  if (entry.isFile() && entry.name.endsWith('.html')) {
    const domain = path.basename(entry.name, '.html');
    const result = await runOne(
      domain,
      path.join(fixturesRoot, entry.name),
      'default',
    );
    if (result === 'error') errors += 1;
    else if (result === 'ok') count += 1;
  }
}

console.log(`Dumped ${count} snapshot(s), ${errors} error(s).`);

async function runOne(domain, htmlPath, basename) {
  const html = fs.readFileSync(htmlPath, 'utf-8');
  const url = `https://${domain}/`;
  try {
    const Mercury = loadMercury();
    const result = await Mercury.parse(url, { html, fallback: false });
    const outDir = path.join(SNAPSHOTS_DIR, domain);
    fs.mkdirSync(outDir, { recursive: true });
    const outPath = path.join(outDir, `${basename}.json`);
    fs.writeFileSync(outPath, JSON.stringify(result, null, 2) + '\n', 'utf-8');
    return 'ok';
  } catch (e) {
    console.error(`[${domain}/${basename}] failed:`, e.message);
    return 'error';
  }
}
