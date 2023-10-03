import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Suppress // to make detekt shut up and stop crashing IDE

plugins {
    kotlin("multiplatform") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("maven-publish")
    id("com.android.library") version "8.1.0"
}

group = "me.darefox"
version = "1.0-SNAPSHOT"

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

enum class OS {
    MacOS,
    Linux,
    Windows,
    Other
}

enum class Arch {
    Arm,
    X86,
    Other
}

data class Machine(
    val system: OS,
    val arch: Arch
) {
    companion object {
        val currentMachine by lazy {
            Machine(getSystem(), getArch())
        }

        private fun getSystem(): OS {
            val hostProperty = System.getProperty("os.name")
            return when {
                hostProperty == "Mac OS X" -> OS.MacOS
                hostProperty == "Linux" -> OS.Linux
                hostProperty.startsWith("Windows") -> OS.Windows
                else -> OS.Other
            }
        }

        private fun getArch(): Arch {
            val arch = System.getProperty("os.arch")
            return when (arch) {
                "amd64", "x86" -> Arch.X86
                "aarch64", "aarch32" -> Arch.Arm
                else -> Arch.Other
            }
        }
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
    fun darwinTargetsX86() = listOf(
        macosX64(),
        iosX64(),
        watchosX64(),
        tvosX64(),
    )
    fun darwinTargetsArm() = listOf(
        macosArm64(),

        iosSimulatorArm64(),
        iosArm64(),

        watchosSimulatorArm64(),
        watchosArm32(),
        watchosArm64(),
        watchosDeviceArm64(),

        tvosSimulatorArm64(),
        tvosArm64(),
    )
    fun linuxTargetsArm() = listOf(linuxArm64())
    fun linuxTargetsX86() = listOf(linuxX64())
    fun windowsTargetsX86() = listOf(mingwX64())

    fun getHostNativeTargets(machine: Machine): List<KotlinNativeTarget> {
        return when(machine) {
            Machine(OS.MacOS, Arch.Arm) -> darwinTargetsArm()
            Machine(OS.MacOS, Arch.X86) -> darwinTargetsX86()
            Machine(OS.Linux, Arch.Arm) -> linuxTargetsArm()
            Machine(OS.Linux, Arch.X86) -> linuxTargetsX86()
            Machine(OS.Windows, Arch.X86) -> windowsTargetsX86()
            else -> listOf()
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
                    if (Machine.currentMachine.system == OS.MacOS) useSafari()
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
    val nativeHostTargets = getHostNativeTargets(Machine.currentMachine)

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
        nativeHostTargets.forEach {
            getByName("${it.targetName}Main") {
                dependsOn(nativeMain)
            }
        }
    }



    fun PublicationContainer.onlyHostCanPublishTheseTargets(
        machine: Machine,
        targets: List<String>
    ) {
        matching { it.name in targets }.all {
            val targetPublication = this@all

            tasks.withType<AbstractPublishToMaven>()
                .matching { (it.publication == targetPublication) }
                .configureEach {
                    onlyIf {
                        val canPublish = Machine.currentMachine == machine
                        when(canPublish) {
                            true ->
                                println("Current host ($machine) can publish ${targetPublication.name} target")
                            false ->
                                println("Only $machine can publish ${targetPublication.name} target")
                        }
                        canPublish
                    }
                }
        }
    }

    publishing {
        publications {
            onlyHostCanPublishTheseTargets(
                machine = Machine(OS.Linux, Arch.X86),
                targets = listOf("androidDebug", "androidRelease", "kotlinMultiplatform", "jvm", "js")
            )
        }
    }
}
