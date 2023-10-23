package publication

import host.Machine
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.kotlin.dsl.withType
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer

fun PublicationContainer.onlyHostCanPublishTheseTargets(
    machine: Machine,
    targets: List<String>,
    tasks: TaskContainer
) {
    matching { it.name in targets }.all {
        val targetPublication = this@all

        tasks.withType<AbstractPublishToMaven>()
            .matching { (it.publication == targetPublication) }
            .configureEach {
                onlyIf {
                    val canPublish = Machine.currentMachine == machine
                    when(canPublish) {
                        true ->
                            println("Current host ($machine) can publish ${targetPublication.name} target")
                        false ->
                            println("Only $machine can publish ${targetPublication.name} target")
                    }
                    canPublish
                }
            }
    }
}