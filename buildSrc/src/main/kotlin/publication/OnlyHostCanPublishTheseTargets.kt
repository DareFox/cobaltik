package publication

import host.Machine
import org.gradle.api.Project
import host.currentMachine
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

fun Project.onlyHostCanPublishTheseTargets(
    publishingMachine: Machine,
    target: List<String>
) {
    val excludedTasks = target.map(::getPublishTaskName)

    if (publishingMachine != currentMachine) {
        val excludedTaskStringList = "- ${excludedTasks.joinToString("\n- ")}"

        println("[Publishing] These tasks are excluded because $currentMachine can't publish these targets:")
        println(excludedTaskStringList)
        gradle.startParameter.excludedTaskNames.addAll(excludedTasks)
    }
}

private fun getPublishTaskName(target: String): String
    = "publish${target.uppercaseFirstChar()}ToMavenLocal"