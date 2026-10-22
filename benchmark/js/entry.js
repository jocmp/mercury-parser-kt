import 'core-js/actual/atob';
import 'core-js/actual/btoa';
import 'core-js/actual/url';
import 'core-js/actual/url-search-params';
import Mercury from 'mercury-src/mercury';

globalThis.mercuryParse = (url, html) =>
  Mercury.parse(url, { html, fetchAllPages: false }).then(result =>
    JSON.stringify(result)
  );
