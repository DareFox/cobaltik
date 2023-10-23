import dependencies.Library
import io.gitlab.arturbosch.detekt.Detekt
import host.Arch
import host.OS
import host.Machine
import multiplatform.nativeSpecificDependencies
import multiplatform.setupJava
import multiplatform.setupJs
import multiplatform.setupNativeTargetsFor
import org.gradle.kotlin.dsl.kotlin
import publication.onlyHostCanPublishTheseTargets

@Suppress // to make detekt shut up and stop crashing IDE

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.9.10"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("maven-publish")
    id("com.android.library")
}

group = "me.darefox"
version = "1.0"

detekt {
    allRules = false
    config.setFrom("$projectDir/config/detekt.yml")
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
        txt.required.set(true) // similar to the CLI output, contains issue signature to manually edit baseline files
        sarif.required.set(true) // standardized SARIF format to support integrations with GitHub Code Scanning
        md.required.set(true) // simple Markdown format
    }
}

android {
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    namespace = "me.darefox.cobaltik"
}

kotlin {
    withSourcesJar()

    setupJava()
    setupJs()
    androidTarget {
        publishLibraryVariants("release", "debug")
    }
    val nativeHostTargets = setupNativeTargetsFor(Machine.currentMachine)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Library.COROUTINES_CORE)
                implementation(Library.KOTLIN_LOGGING)
                implementation(Library.KTOR_CLIENT_CORE)
                implementation(Library.KTOR_CLIENT_NEGOTIATION)
                implementation(Library.KTOR_SERIALIZATION_JSON)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies.runtimeOnly(Library.KTOR_CLIENT_ANDROID)
        }
        val androidTest by creating
        val jvmMain by getting {
            dependencies.runtimeOnly(Library.KTOR_CLIENT_CIO)
        }
        val jvmTest by getting {
            dependencies.runtimeOnly(Library.SLF4J_SIMPLE)
        }
        val jsMain by getting {
            dependencies.runtimeOnly(Library.KTOR_CLIENT_JS)
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nativeMain by creating {
            dependencies.runtimeOnly(Library.KTOR_CLIENT_CIO)
        }
        val nativeTest by creating
        nativeSpecificDependencies(
            nativeHostTargets = nativeHostTargets,
            nativeMainSourceSet = nativeMain
        )
    }

    publishing {
        publications {
            onlyHostCanPublishTheseTargets(
                machine = Machine(OS.Linux, Arch.X86),
                targets = listOf("androidDebug", "androidRelease", "kotlinMultiplatform", "jvm", "js"),
                tasks = tasks
            )
        }
    }
}
