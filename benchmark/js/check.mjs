import fs from 'node:fs';
import path from 'node:path';
import vm from 'node:vm';
import { fileURLToPath } from 'node:url';

const HERE = path.dirname(fileURLToPath(import.meta.url));
const BUNDLE = path.join(HERE, '..', 'app', 'src', 'main', 'assets', 'mercury.bundle.js');
const FIXTURE = path.join(
  HERE, '..', '..', 'mercury-parser', 'src', 'test', 'resources', 'fixtures', 'deadline.com.html',
);

const context = vm.createContext({});
vm.runInContext(fs.readFileSync(BUNDLE, 'utf-8'), context);

const html = fs.readFileSync(FIXTURE, 'utf-8');
context.html = html;
const json = await vm.runInContext("mercuryParse('https://deadline.com/', html)", context);
const result = JSON.parse(json);
console.log(result.title, '|', result.author, '|', result.word_count);
