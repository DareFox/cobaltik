package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CobaltServerInfo(
    /**
     * Cobalt version
     */
    val version: String,
    /**
     * Git commit
     */
    val commit: String,
    /**
     * Git branch
     */
    val branch: String,
    /**
     * Server name
     */
    val name: String,
    /**
     * CORS status
     */
    val cors: String,
    /**
     * Server start time
     */
    val startTime: String,
)