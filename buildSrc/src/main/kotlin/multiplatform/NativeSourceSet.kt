package multiplatform

import dependencies.LibraryVersions
import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun NamedDomainObjectContainer<KotlinSourceSet>.nativeSpecificDependencies(
    nativeHostTargets: List<KotlinNativeTarget>,
    nativeMainSourceSet: KotlinSourceSet
) {
    nativeHostTargets.forEach {
        getByName("${it.targetName}Main") {
            dependsOn(nativeMainSourceSet)
            dependencies {
                val target = it.targetName.lowercase()
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-$target:${LibraryVersions.COROUTINES}")
            }
        }
    }
}