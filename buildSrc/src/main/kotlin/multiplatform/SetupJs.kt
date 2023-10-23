package multiplatform

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun KotlinMultiplatformExtension.setupJs() {
    js {
        browser {
            generateTypeScriptDefinitions()
            testTask {
                useKarma {
                    useFirefoxHeadless()
                }
            }
        }
        nodejs {
            generateTypeScriptDefinitions()
            testTask {
                useMocha()
            }
        }
    }
}