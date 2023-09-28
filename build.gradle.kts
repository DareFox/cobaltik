import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("maven-publish")
    id("com.android.library") version "8.1.0"
}

group = "me.darefox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

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
}

enum class Host {
    MacOS,
    Linux,
    Windows,
    Other
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
    namespace = "me.darefox.cobaltik"
}

kotlin {
    fun darwinTargets() = listOf(
        macosX64("native"),
        macosArm64("native"),

        iosSimulatorArm64("native"),
        iosX64("native"),
        iosArm64("native"),

        watchosSimulatorArm64("native"),
        watchosX64("native"),
        watchosArm32("native"),
        watchosArm64("native"),
        watchosDeviceArm64("native"),

        tvosSimulatorArm64("native"),
        tvosX64("native"),
        tvosArm64("native"),
    )

    fun linuxTargets() = listOf(
        linuxX64("native"),
        linuxArm64("native")
    )

    fun windowsTargets() = listOf(
        mingwX64("native")
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
    androidTarget {
        publishLibraryVariants("release", "debug")
    }
    getHostNativeTargets(getCurrentHost())

    sourceSets {
        val commonMain by getting {
            dependencies {
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
        val jsTest by getting
        val nativeMain by getting {
            dependencies {
                runtimeOnly("io.ktor:ktor-client-cio:${LibVersions.KTOR}")
            }
        }
        val nativeTest by getting
    }

    fun PublicationContainer.onlyHostCanPublishTheseTargets(host: Host, targets: List<KotlinTarget>) {
        val stringTargets = targets.map { it.name } + "kotlinMultiplatform"
        matching { it.name in stringTargets }.all {
            val targetPublication = this@all

            tasks.withType<AbstractPublishToMaven>()
                .matching { it.publication == targetPublication }
                .configureEach {
                    onlyIf {
                        val canPublish = getCurrentHost() == host
                        when(canPublish) {
                            true ->
                                println("Current host (${host.name}) can publish ${targetPublication.name} target")
                            false ->
                                println("Only ${host.name} can publish ${targetPublication.name} target")
                        }
                        canPublish
                    }
                }
        }
    }

    publishing {
        publications {
            onlyHostCanPublishTheseTargets(Host.Linux, listOf(jvm(), js()))
        }
    }
}
