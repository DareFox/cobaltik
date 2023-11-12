plugins {
    `kotlin-dsl`
}

repositories.gradlePluginPortal()
repositories.google()
repositories.mavenCentral()

val KOTLIN_VER = providers.gradleProperty("kotlin.version").get()
val ANDROD_TOOLS_VER = "8.1.0"

dependencies {
    implementation("org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin:$KOTLIN_VER")
    implementation("com.android.tools.build:gradle:$ANDROD_TOOLS_VER")
}