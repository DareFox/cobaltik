package gradle

fun getBooleanProperty(property: String): Boolean? = System.getProperty(property)
    ?.lowercase()
    ?.toBooleanStrictOrNull()