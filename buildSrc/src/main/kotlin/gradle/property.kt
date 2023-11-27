package gradle

fun getBooleanEnv(variable: String): Boolean? = System.getenv(variable)
    ?.lowercase()
    ?.toBooleanStrictOrNull()