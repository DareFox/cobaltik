package multiplatform

import host.Arch
import host.Machine
import host.OS
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.setupNativeTargetsFor(machine: Machine): List<KotlinNativeTarget> {
    return when(machine) {
        Machine(OS.MacOS, Arch.Arm),
        Machine(OS.MacOS, Arch.X86) -> darwinTargetsX86() + darwinTargetsArm()
        Machine(OS.Linux, Arch.Arm) -> linuxTargetsArm()
        Machine(OS.Linux, Arch.X86) -> linuxTargetsX86()
        Machine(OS.Windows, Arch.X86) -> windowsTargetsX86()
        else -> listOf()
    }
}

