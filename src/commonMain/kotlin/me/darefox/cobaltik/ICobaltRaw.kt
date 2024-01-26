package me.darefox.cobaltik

import me.darefox.cobaltik.models.CobaltRequest
import me.darefox.cobaltik.models.CobaltResponse
import me.darefox.cobaltik.models.CobaltServerInfo

interface ICobaltRaw {
    val serverUrl: String

    /**
     * POST `/api/json`
     *
     * Makes a request to the main processing endpoint
     *
     * This function sends a [CobaltRequest] to the server and expects a [CobaltResponse] in return.
     * If the server returns an error, a [CobaltError] is thrown.
     *
     * @param request The [CobaltRequest] to send to the server.
     * @return The [CobaltResponse] received from the server.
     * @see <a href="https://github.com/wukko/cobalt/blob/current/docs/API.md#post-apijson">Documentation</a>
     */
    suspend fun request(request: CobaltRequest): CobaltResponse

    /**
     * GET `/api/serverInfo`
     *
     * Retrieves current basic server information.
     *
     * This function sends a GET request to retrieve basic information about the Cobalt server.
     *
     * @return The [CobaltServerInfo] containing basic server information.
     * @see <a href="https://github.com/wukko/cobalt/blob/current/docs/API.md#get-apiserverinfo">Documentation</a>
     */
    suspend fun getServerInfo(): CobaltServerInfo
}