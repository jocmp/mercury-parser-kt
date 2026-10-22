import fs from 'node:fs';
import path from 'node:path';
import process from 'node:process';
import { fileURLToPath } from 'node:url';
import * as esbuild from 'esbuild';

const HERE = path.dirname(fileURLToPath(import.meta.url));
const MERCURY_DIR = path.resolve(
  process.env.MERCURY_PARSER_DIR || path.join(HERE, '..', '..', '..', 'mercury-parser'),
);
const SRC = path.join(MERCURY_DIR, 'src');
const OUT = path.join(HERE, '..', 'app', 'src', 'main', 'assets', 'mercury.bundle.js');

const srcAlias = {
  name: 'mercury-src',
  setup(build) {
    build.onResolve({ filter: /^[^./]/ }, args => {
      let source = args.path;
      if (source.startsWith('mercury-src/')) {
        source = source.slice('mercury-src/'.length);
      }
      const base = path.join(SRC, source);
      const match = [`${base}.js`, path.join(base, 'index.js')].find(
        candidate => fs.existsSync(candidate) && fs.statSync(candidate).isFile(),
      );
      if (match) {
        return { path: match };
      }
      return undefined;
    });
  },
};

await esbuild.build({
  entryPoints: [path.join(HERE, 'entry.js')],
  bundle: true,
  format: 'iife',
  platform: 'browser',
  target: 'es2020',
  minify: process.env.MINIFY !== '0',
  outfile: OUT,
  nodePaths: [path.join(MERCURY_DIR, 'node_modules')],
  alias: {
    'postman-request': path.join(HERE, 'request-stub.js'),
    'iconv-lite': path.join(SRC, 'shims', 'iconv-lite.js'),
  },
  define: {
    'process.env.NODE_ENV': '"production"',
    global: 'globalThis',
  },
  plugins: [srcAlias],
  logLevel: 'warning',
});

console.log(`${OUT} ${(fs.statSync(OUT).size / 1024).toFixed(0)} KB`);
