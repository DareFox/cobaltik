package me.darefox.cobaltik

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import me.darefox.cobaltik.models.CobaltError
import me.darefox.cobaltik.models.CobaltRequest
import me.darefox.cobaltik.models.CobaltResponse
import me.darefox.cobaltik.models.CobaltServerInfo

/**
 * A client for making requests to a Cobalt server.
 *
 * @param serverUrl The base URL of the Cobalt server.
 * @see <a href="https://github.com/wukko/cobalt/blob/current/docs/API.md">Cobalt API Documentation</a>
 */
class CobaltRaw(override val serverUrl: String) : ICobaltRaw {
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
    override suspend fun request(request: CobaltRequest): CobaltResponse {
        val response = ktor.use { client ->
            client.post {
                url(appendPath("/api/json"))
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<CobaltResponse>()
        }

        return response
    }

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
    override suspend fun getServerInfo(): CobaltServerInfo = ktor.use { client ->
        client.get {
            url(appendPath("/api/serverInfo"))
        }.body()
    }


    private fun appendPath(path: String): String {
        var base = serverUrl
        if (path.startsWith("/") && serverUrl.endsWith("/")) {
            base = serverUrl.removeSuffix("/")
        }

        return base + path
    }
}
