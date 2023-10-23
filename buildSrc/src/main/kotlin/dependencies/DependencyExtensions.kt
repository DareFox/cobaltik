package dependencies

import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

fun KotlinSourceSet.kotlinRuntimeOnly(dependencyNotation: String) {
    dependencies { runtimeOnly(dependencyNotation) }
}