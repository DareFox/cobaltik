package host

enum class Arch {
    Arm,
    X86,
    Other
}

fun getCurrentArch(): Arch {
    val arch = System.getProperty("os.arch")
    println("getArch(): os.arch = $arch")
    return when (arch) {
        "amd64", "x86", "x86_64" -> Arch.X86
        "aarch64", "aarch32" -> Arch.Arm
        else -> Arch.Other
    }
}