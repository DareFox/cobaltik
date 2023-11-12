import dependencies.Library
import dependencies.LibraryVersions
import dependencies.kotlinRuntimeOnly
import io.gitlab.arturbosch.detekt.Detekt
import host.*
import multiplatform.*
import publication.onlyHostCanPublishTheseTargets

@Suppress // to make detekt shut up and stop crashing IDE

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.9.20"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    `maven-publish`
    id("com.android.library")
    signing
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
    val nativeHostTargets = setupNativeTargetsFor(currentMachine)

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
            kotlinRuntimeOnly(Library.KTOR_CLIENT_ANDROID)
        }
        val androidTest by creating
        val jvmMain by getting {
            kotlinRuntimeOnly(Library.KTOR_CLIENT_CIO)
        }
        val jvmTest by getting {
            kotlinRuntimeOnly(Library.SLF4J_SIMPLE)
        }
        val jsMain by getting {
            kotlinRuntimeOnly(Library.KTOR_CLIENT_JS)
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nativeMain by creating {
            dependsOn(commonMain)
            kotlinRuntimeOnly(Library.KTOR_CLIENT_CIO)
        }
        val nativeTest by creating
        nativeSpecificDependencies(
            nativeHostTargets = nativeHostTargets,
            nativeMainSourceSet = nativeMain
        )
    }
}


onlyHostCanPublishTheseTargets(
    publishingMachine = Machine(OS.Linux, Arch.X86),
    target = listOf("androidDebug", "androidRelease", "kotlinMultiplatform", "jvm", "js")
)

signing {
    val gpgPublicId = System.getProperty("MAVEN_GPG_PUBLIC_KEY_ID")
    val gpgPrivateKey = System.getProperty("MAVEN_GPG_PRIVATE_KEY")
    val gpgPrivatePassword = System.getProperty("MAVEN_GPG_PRIVATE_PASSWORD")

    val envVariables = listOf(gpgPrivateKey, gpgPublicId, gpgPrivatePassword)

    val allEnvVariablesSet = envVariables.all { it != null }
    val someEnvVariablesSet = envVariables.any { it != null }

    when {
        allEnvVariablesSet -> {
            println("[Singing] Using in memory gpg keys")
            useInMemoryPgpKeys(
                gpgPublicId,
                gpgPrivateKey,
                gpgPrivatePassword
            )
        }
        someEnvVariablesSet -> throw Error("Some singing variables set, but not all of them")
        else -> {
            println("[Singing] Using system-wide gpg")
            useGpgCmd()
        }
    }

    sign(publishing.publications)
}
