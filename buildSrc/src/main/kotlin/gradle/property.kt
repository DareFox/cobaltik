package gradle

fun System.getBooleanProperty(property: String): Boolean? = System.getProperty(property)
    ?.lowercase()
    ?.toBooleanStrictOrNull()