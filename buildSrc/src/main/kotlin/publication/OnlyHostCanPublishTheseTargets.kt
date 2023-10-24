package publication

import host.Machine
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.kotlin.dsl.withType
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import host.currentMachine
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

fun Project.onlyHostCanPublishTheseTargets(
    publishingMachine: Machine,
    target: List<String>
) {
    val excludedTasks = target.map(::getPublishTaskName)

    if (publishingMachine != currentMachine) {
        println("Excluded tasks $excludedTasks because $currentMachine can't publish those targets")
        gradle.startParameter.excludedTaskNames.addAll(excludedTasks)
    }
}

private fun getPublishTaskName(target: String): String
    = "publish${target.uppercaseFirstChar()}ToMavenLocal"