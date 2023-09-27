import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("maven-publish")
}

group = "me.darefox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

detekt {
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
        txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
        sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
        md.required.set(true) // simple Markdown format
    }
}

object LibVersions {
    val ktorVersion = "2.2.4"
    val kotlingLogging = "5.0.1"
    val slf4jSimple = "2.0.3"
}

enum class Host {
    MacOS,
    Linux,
    Windows,
    Other
}

kotlin {
    fun darwinTargets() = listOf(
        macosX64(),
        macosArm64(),

        iosSimulatorArm64(),
        iosX64(),
        iosArm64(),

        watchosSimulatorArm64(),
        watchosX64(),
        watchosArm32(),
        watchosArm64(),
        watchosDeviceArm64(),

        tvosSimulatorArm64(),
        tvosX64(),
        tvosArm64(),
    )

    fun linuxTargets() = listOf(
        linuxX64(),
        linuxArm64()
    )

    fun windowsTargets() = listOf(
        mingwX64()
    )

    fun getCurrentHost(): Host {
        val hostProperty = System.getProperty("os.name")
        return when {
            hostProperty == "Mac OS X" -> Host.MacOS
            hostProperty == "Linux" -> Host.Linux
            hostProperty.startsWith("Windows") -> Host.Windows
            else -> Host.Other
        }
    }

    fun getHostNativeTargets(host: Host): List<KotlinNativeTarget> {
        return when(host) {
            Host.MacOS -> darwinTargets()
            Host.Linux -> linuxTargets()
            Host.Windows -> windowsTargets()
            Host.Other -> listOf()
        }
    }

    jvm {
        jvmToolchain(8)
        withJava()
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
                    if (getCurrentHost() == Host.MacOS) useSafari()
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
    getHostNativeTargets(getCurrentHost())

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.oshai:kotlin-logging:${LibVersions.kotlingLogging}")
                implementation("io.ktor:ktor-client-core:${LibVersions.ktorVersion}")
                implementation("io.ktor:ktor-client-content-negotiation:${LibVersions.ktorVersion}")
                implementation("io.ktor:ktor-serialization-kotlinx-json:${LibVersions.ktorVersion}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                runtimeOnly("io.ktor:ktor-client-cio:${LibVersions.ktorVersion}")
            }
        }
        val jvmTest by getting {
            dependencies {
                runtimeOnly("org.slf4j:slf4j-simple:${LibVersions.slf4jSimple}")
            }
        }
        val jsMain by getting {
            dependencies {
                runtimeOnly("io.ktor:ktor-client-js:${LibVersions.ktorVersion}")
            }
        }
        val jsTest by getting
        val nativeMain by creating {
            dependencies {
                runtimeOnly("io.ktor:ktor-client-cio:${LibVersions.ktorVersion}")
            }
        }
        val nativeTest by creating
    }

    fun PublicationContainer.onlyHostCanPublishTheseTargets(host: Host, targets: List<KotlinTarget>) {
        val stringTargets = targets.map { it.name } + "kotlinMultiplatform"
        matching { it.name in stringTargets }.all {
            val targetPublication = this@all

            tasks.withType<AbstractPublishToMaven>()
                .matching { it.publication == targetPublication }
                .configureEach { onlyIf { getCurrentHost() == host } }
        }
    }

    publishing {
        publications {
            onlyHostCanPublishTheseTargets(Host.Linux, listOf(jvm(), js()))
        }
    }
}
