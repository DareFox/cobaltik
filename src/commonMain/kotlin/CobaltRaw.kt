import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import models.CobaltError
import models.CobaltRequest
import models.CobaltResponse
import models.CobaltResponseStatus

class CobaltRaw(val serverUrl: String) {
    /**
     *  # POST /api/json
     *  Main processing endpoint
     *  @see <a href="https://github.com/wukko/cobalt/blob/current/docs/API.md#post-apijson">Documentation</a>
     *  @throws CobaltError If server returns error
     */
    suspend fun request(request: CobaltRequest): CobaltResponse {
        val response = ktor.use { client ->
            client.post {
                url(appendPath("/api/json"))
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<CobaltResponse>()
        }


        if (response.status == CobaltResponseStatus.ERROR) {
            throw CobaltError(response.text ?: "[No error text]")
        }

        return response
    }

    private fun appendPath(path: String): String {
        var base = serverUrl
        if (path.startsWith("/") && serverUrl.endsWith("/")) {
            base = serverUrl.removeSuffix("/")
        }

        return base + path
    }
}