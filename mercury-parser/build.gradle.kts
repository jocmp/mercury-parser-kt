plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.1.0"
    `java-library`
    id("com.vanniktech.maven.publish") version "0.30.0"
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    api("org.jsoup:jsoup:1.18.3")
    api("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
    systemProperty("junit.jupiter.execution.parallel.enabled", "true")
    systemProperty("junit.jupiter.execution.parallel.mode.default", "concurrent")
    systemProperty("junit.jupiter.execution.parallel.mode.classes.default", "concurrent")
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL, true)
    signAllPublications()

    coordinates(project.group.toString(), "mercury-parser", project.version.toString())

    pom {
        name.set("mercury-parser")
        description.set("A Kotlin port of Mercury Parser. Extracts the meaningful content from a web page.")
        url.set("https://github.com/jocmp/mercury-parser-kt")
        inceptionYear.set("2026")

        scm {
            connection.set("scm:git:git://github.com/jocmp/mercury-parser-kt.git")
            developerConnection.set("scm:git:git@github.com:jocmp/mercury-parser-kt.git")
            url.set("https://github.com/jocmp/mercury-parser-kt")
        }

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("jocmp")
                name.set("Josiah Campbell")
                email.set("hello@jocmp.com")
            }
        }
    }
}
