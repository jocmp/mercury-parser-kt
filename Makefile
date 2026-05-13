GRADLE := ./gradlew
CLI_BIN := cli/build/install/mercury/bin/mercury

.DEFAULT_GOAL := build

.PHONY: build test lint format check cli clean dist parse help

build: $(CLI_BIN)

$(CLI_BIN):
	$(GRADLE) :cli:installDist

test:
	$(GRADLE) test

lint:
	$(GRADLE) ktlintCheck

format:
	$(GRADLE) ktlintFormat

check: lint test

cli: $(CLI_BIN)

# Usage: make parse URL=https://example.com [HTML=path/to/file.html] [FORMAT=html]
parse: $(CLI_BIN)
	@if [ -z "$(URL)" ]; then echo "URL=... is required" >&2; exit 2; fi
	@$(CLI_BIN) --url "$(URL)" $(if $(HTML),--html "$(HTML)") $(if $(FORMAT),--format "$(FORMAT)")

dist:
	$(GRADLE) :cli:distTar :cli:distZip

clean:
	$(GRADLE) clean

help:
	@echo "Targets:"
	@echo "  build    - build the CLI (default)"
	@echo "  test     - run tests"
	@echo "  lint     - run ktlint check"
	@echo "  format   - apply ktlint formatting"
	@echo "  check    - lint + test"
	@echo "  cli      - install CLI to $(CLI_BIN)"
	@echo "  parse    - run parser: make parse URL=https://... [HTML=file.html] [FORMAT=html]"
	@echo "  dist     - build distributable tar/zip"
	@echo "  clean    - clean build outputs"
