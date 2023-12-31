package multiplatform

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension


fun KotlinMultiplatformExtension.darwinTargetsX86() = listOf(
    macosX64(),
    iosX64(),
    watchosX64(),
    tvosX64(),
)
fun KotlinMultiplatformExtension.darwinTargetsArm() = listOf(
    macosArm64(),

    iosSimulatorArm64(),
    iosArm64(),

    watchosSimulatorArm64(),
    watchosArm64(),

    tvosSimulatorArm64(),
    tvosArm64(),
)
fun KotlinMultiplatformExtension.linuxTargetsArm() = listOf(linuxArm64())
fun KotlinMultiplatformExtension.linuxTargetsX86() = listOf(linuxX64())
fun KotlinMultiplatformExtension.windowsTargetsX86() = listOf(mingwX64())