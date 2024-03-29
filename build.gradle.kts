import dependencies.Library
import dependencies.kotlinRuntimeOnly
import gradle.getBooleanEnv
import gradle.onlyHostCanDoTheseTasks
import host.Arch
import host.Machine
import host.OS
import host.currentMachine
import io.gitlab.arturbosch.detekt.Detekt
import multiplatform.nativeSpecificDependencies
import multiplatform.setupJava
import multiplatform.setupJs
import multiplatform.setupNativeTargetsFor
import publication.onlyHostCanPublishTheseTargets

@Suppress // to make detekt shut up and stop crashing IDE

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.9.20"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    `maven-publish`
    id("org.jetbrains.dokka") version "1.9.10"
    signing
}

val isPublishing = getBooleanEnv("IS_PUBLISHING") ?: false
val isSnapshot = getBooleanEnv("IS_SNAPSHOT") ?: true
val propertyVersion = providers.gradleProperty("project.version").get()

println("isPublishing: $isPublishing")
println("isSnapshot: $isSnapshot")
println("propertyVersion: $propertyVersion")

group = "me.darefox"
version = propertyVersion + if (isSnapshot) "-SNAPSHOT" else ""

println("version: $version")

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

tasks {
    register<Jar>("dokkaJar") {
        from(dokkaHtml)
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
    }
}

kotlin {
    withSourcesJar()

    setupJava()
    setupJs()
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
        val jvmMain by getting {
            kotlinRuntimeOnly(Library.KTOR_CLIENT_OKHTTP)
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
            dependsOn(commonMain) // WITHOUT THIS LINE GRADLE WON'T CROSSCOMPILE
            kotlinRuntimeOnly(Library.KTOR_CLIENT_OKHTTP)
        }
        val nativeTest by creating
        nativeSpecificDependencies(
            nativeHostTargets = nativeHostTargets,
            nativeMainSourceSet = nativeMain
        )
    }
}

// TESTING
onlyHostCanDoTheseTasks(
    machine = Machine(OS.Linux, Arch.X86),
    tasks = listOf("jvmTest"), // android + jvm
    reasonForExcluding = "$currentMachine will skip these targets to remove duplication of tests"
)

publishing {
    repositories {
        if (isPublishing) {
            onlyHostCanPublishTheseTargets(
                publishingMachine = Machine(OS.Linux, Arch.X86),
                target = listOf("kotlinMultiplatform", "jvm", "js")
            )

            maven {
                name = "sonatype"
                val repourl = if (isSnapshot) {
                    uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                } else {
                    uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                }
                println("repo url is $repourl")
                url = repourl
                credentials {
                    username = System.getenv("SONATYPE_USERNAME") ?: throw Error("env SONATYPE_USERNAME is empty")
                    password = System.getenv("SONATYPE_PASSWORD") ?: throw Error("env SONATYPE_PASSWORD is empty")
                }
            }
        }
    }

    publications.withType<MavenPublication> {
        pom {
            name.set("Cobaltik")
            description.set("Kotlin multi-platform library wrapper for cobalt (media downloader) API")
            url.set("https://github.com/DareFox/cobaltik")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/license/MIT/")
                }
            }
            developers {
                developer {
                    id.set("darefox")
                    name.set("DareFox")
                    email.set("darefoxdev@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/DareFox/cobaltik.git")
                developerConnection.set("scm:git:ssh://github.com/DareFox/cobaltik.git")
                url.set("https://github.com/DareFox/cobaltik")
            }
        }
        artifact(tasks["dokkaJar"])
    }
}

// see https://youtrack.jetbrains.com/issue/KT-46466
tasks.withType(AbstractPublishToMaven::class.java).configureEach {
    dependsOn(tasks.withType(Sign::class.java))
}

signing {
    val gpgPublicId = System.getenv("MAVEN_GPG_PUBLIC_KEY_ID")
    val gpgPrivateKey = System.getenv("MAVEN_GPG_PRIVATE_KEY")
    val gpgPrivatePassword = System.getenv("MAVEN_GPG_PRIVATE_PASSWORD")

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
