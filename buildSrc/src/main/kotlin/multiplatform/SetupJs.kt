package multiplatform

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun KotlinMultiplatformExtension.setupJs() = js {
    browser {
        generateTypeScriptDefinitions()
    }
    nodejs {
        generateTypeScriptDefinitions()
        testTask {
            useMocha()
        }
    }
}