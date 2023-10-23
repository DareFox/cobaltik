package host

enum class OS {
    MacOS,
    Linux,
    Windows,
    Other
}

fun getCurrentSystem(): OS {
    val hostProperty = System.getProperty("os.name")
    println("getSystem(): os.name = $hostProperty")
    return when {
        hostProperty == "Mac OS X" -> OS.MacOS
        hostProperty == "Linux" -> OS.Linux
        hostProperty.startsWith("Windows") -> OS.Windows
        else -> OS.Other
    }
}
