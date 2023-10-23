package host

data class Machine(
    val system: OS,
    val arch: Arch
) {
    companion object {
        val currentMachine by lazy {
            Machine(getSystem(), getArch())
        }

        private fun getSystem(): OS {
            val hostProperty = System.getProperty("os.name")
            println("getSystem(): os.name = $hostProperty")
            return when {
                hostProperty == "Mac OS X" -> OS.MacOS
                hostProperty == "Linux" -> OS.Linux
                hostProperty.startsWith("Windows") -> OS.Windows
                else -> OS.Other
            }
        }

        private fun getArch(): Arch {
            val arch = System.getProperty("os.arch")
            println("getArch(): os.arch = $arch")
            return when (arch) {
                "amd64", "x86", "x86_64" -> Arch.X86
                "aarch64", "aarch32" -> Arch.Arm
                else -> Arch.Other
            }
        }
    }
}