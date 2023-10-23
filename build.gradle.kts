import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import host.Arch
import host.OS
import host.Machine
import multiplatform.nativeSpecificDependencies
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

object LibVersions {
    const val KTOR = "2.2.4"
    const val KOTLIN_LOGGING = "5.0.1"
    const val SLF4J_SIMPLE = "2.0.3"
    const val COROUTINES = "1.7.3"
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

    jvm {
        jvmToolchain(8)
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }

    }
    js {
        browser {
            generateTypeScriptDefinitions()
            testTask(Action {
                useKarma {
                    useFirefoxHeadless()
                }
            })
        }
        nodejs() {
            generateTypeScriptDefinitions()
            testTask(Action {
                useMocha()
            })
        }
    }
    androidTarget {
        publishLibraryVariants("release", "debug")
    }
    val nativeHostTargets = setupNativeTargetsFor(Machine.currentMachine)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibVersions.COROUTINES}")
                implementation("io.github.oshai:kotlin-logging:${LibVersions.KOTLIN_LOGGING}")
                implementation("io.ktor:ktor-client-core:${LibVersions.KTOR}")
                implementation("io.ktor:ktor-client-content-negotiation:${LibVersions.KTOR}")
                implementation("io.ktor:ktor-serialization-kotlinx-json:${LibVersions.KTOR}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                runtimeOnly("io.ktor:ktor-client-android:${LibVersions.KTOR}")
            }
        }
        val androidTest by creating
        val jvmMain by getting {
            dependencies {
                runtimeOnly("io.ktor:ktor-client-cio:${LibVersions.KTOR}")
            }
        }
        val jvmTest by getting {
            dependencies {
                runtimeOnly("org.slf4j:slf4j-simple:${LibVersions.SLF4J_SIMPLE}")
            }
        }
        val jsMain by getting {
            dependencies {
                runtimeOnly("io.ktor:ktor-client-js:${LibVersions.KTOR}")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nativeMain by creating {
            dependencies {
                runtimeOnly("io.ktor:ktor-client-cio:${LibVersions.KTOR}")
            }
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
