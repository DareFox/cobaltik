import io.gitlab.arturbosch.detekt.Detekt

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

kotlin {
    val hostOs = System.getProperty("os.name")
    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val isMingwX64 = hostOs.startsWith("Windows")

    val nativeTarget = when {
        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
        hostOs == "Linux" && isArm64 -> linuxArm64("native")
        hostOs == "Linux" && !isArm64 -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
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
                    if (hostOs == "Mac OS X") useSafari()
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
        val nativeMain by getting {
            dependencies {
                runtimeOnly("io.ktor:ktor-client-cio:${LibVersions.ktorVersion}")
            }
        }
        val nativeTest by getting
    }
}
