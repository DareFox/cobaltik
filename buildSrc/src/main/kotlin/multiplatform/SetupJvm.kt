package multiplatform

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun KotlinMultiplatformExtension.setupJava() = jvm {
    jvmToolchain(8)
    testRuns.named("test") {
        executionTask.configure {
            useJUnitPlatform()
        }
    }
}