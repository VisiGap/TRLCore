import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `maven-publish`
    id("io.papermc.paperweight.patcher") version "2.0.0-beta.18"
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"
val trlcoreMavenUrl = "https://maven.leafmc.one/snapshots/" // TRLCore-Finally Maven

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
        maven(trlcoreMavenUrl)
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
        options.isFork = true
        options.compilerArgs.addAll(listOf("-Xlint:-deprecation", "-Xlint:-removal"))
    }
    tasks.withType<Javadoc>().configureEach {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources>().configureEach {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test>().configureEach {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    extensions.configure<PublishingExtension> {
        repositories {
            maven(trlcoreMavenUrl) {
                name = "trlcore-finally"

                credentials.username = System.getenv("REPO_USER")
                credentials.password = System.getenv("REPO_PASSWORD")
            }
        }
    }
}

paperweight {
    upstreams.paper {
        ref = providers.gradleProperty("paperCommit")

        patchFile {
            path = "paper-server/build.gradle.kts"
            outputFile = file("trlcore-finally-server/build.gradle.kts")
            patchFile = file("trlcore-finally-server/build.gradle.kts.patch")
        }
        patchFile {
            path = "paper-api/build.gradle.kts"
            outputFile = file("trlcore-finally-api/build.gradle.kts")
            patchFile = file("trlcore-finally-api/build.gradle.kts.patch")
        }
        patchDir("paperApi") {
            upstreamPath = "paper-api"
            excludes = setOf("build.gradle.kts")
            patchesDir = file("trlcore-finally-api/paper-patches")
            outputDir = file("paper-api")
        }
    }
}
