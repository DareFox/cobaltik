package publication

import host.Machine
import org.gradle.api.Project
import host.currentMachine
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import gradle.excludeTasks

fun Project.onlyHostCanPublishTheseTargets(
    publishingMachine: Machine,
    target: List<String>
) {
    val excludedTasks = target.map(::getPublishTaskName).flatten()

    // Machine is just data class with two system properties (OS and Architecture)
    if (publishingMachine != currentMachine) {
        excludeTasks(excludedTasks, "$currentMachine can't publish these targets")
    }
}

private fun getPublishTaskName(target: String)
    = listOf(
    "publish${target.uppercaseFirstChar()}PublicationToMavenLocal",
    "publish${target.uppercaseFirstChar()}PublicationToSonatypeRepository")