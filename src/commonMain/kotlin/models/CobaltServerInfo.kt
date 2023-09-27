package models

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
     * Server url
     */
    val url: String,
    /**
     * CORS status
     */
    val cors: Int,
    /**
     * Server start time
     */
    val startTime: String,
)
