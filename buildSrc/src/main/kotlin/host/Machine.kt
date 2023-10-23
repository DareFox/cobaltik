package host

data class Machine(
    val system: OS,
    val arch: Arch
)

val currentMachine by lazy {
    Machine(getCurrentSystem(), getCurrentArch())
}