# mercury-parser-kt

A Kotlin port of [Mercury Parser](https://github.com/jocmp/mercury-parser). Extracts the meaningful content from a web page.

## Modules

- `mercury-parser/` — the library
- `cli/` — command-line interface

## Setup

Gradle:
```kotlin
dependencies {
    implementation("com.jocmp:mercury-parser:0.1.0")
}
```

## Usage

```kotlin
import com.jocmp.mercury.Mercury

suspend fun main() {
    val result = Mercury.parse("https://example.com/article")
    println(result.title)
    println(result.content)
}
```

## CLI

```
./gradlew :cli:installDist
./cli/build/install/mercury/bin/mercury --url https://example.com/article
```

## License

MIT. See [LICENSE](LICENSE).
