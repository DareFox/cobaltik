package dependencies

object Library {
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibraryVersions.COROUTINES}"
    const val KOTLIN_LOGGING = "io.github.oshai:kotlin-logging:${LibraryVersions.KOTLIN_LOGGING}"
    const val SLF4J_SIMPLE ="org.slf4j:slf4j-simple:${LibraryVersions.SLF4J_SIMPLE}"
    const val KTOR_CLIENT_CORE = "io.ktor:ktor-client-core:${LibraryVersions.KTOR}"
    const val KTOR_CLIENT_NEGOTIATION = "io.ktor:ktor-client-content-negotiation:${LibraryVersions.KTOR}"
    const val KTOR_SERIALIZATION_JSON = "io.ktor:ktor-serialization-kotlinx-json:${LibraryVersions.KTOR}"
    const val KTOR_CLIENT_CIO = "io.ktor:ktor-client-cio:${LibraryVersions.KTOR}"
    const val KTOR_CLIENT_ANDROID = "io.ktor:ktor-client-android:${LibraryVersions.KTOR}"
    const val KTOR_CLIENT_JS = "io.ktor:ktor-client-js:${LibraryVersions.KTOR}"
}