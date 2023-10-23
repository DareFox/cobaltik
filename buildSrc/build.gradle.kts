plugins {
    `kotlin-dsl`
}

repositories.gradlePluginPortal()
repositories.google()
repositories.mavenCentral()

dependencies {
    implementation("org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin:1.9.10")
    implementation("com.android.tools.build:gradle:8.1.0")
}