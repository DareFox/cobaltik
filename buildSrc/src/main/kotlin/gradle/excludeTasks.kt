package gradle

import host.Machine
import host.currentMachine
import org.gradle.api.Project

fun Project.excludeTasks(tasks: List<String>, reason: String? = null) {
    gradle.startParameter.excludedTaskNames.addAll(tasks)
    val excludedTaskStringList = "- ${tasks.joinToString("\n- ")}"

    println("These tasks below are excluded. Reason: ${reason ?: "not specified"}")
    println(excludedTaskStringList)
}

fun Project.onlyHostCanDoTheseTasks(machine: Machine, tasks: List<String>, reasonForExcluding: String? = null) {
    if (machine != currentMachine) {
        excludeTasks(tasks, reasonForExcluding)
    }
}