package wrapper

import CobaltRaw
import models.CobaltRequest
import models.CobaltServerInfo
import wrapper.builders.CobaltRequestBuilder
import wrapper.builders.RequestBuilder

/**
 * The `Cobalt` class provides a high-level API for making requests to a Cobalt server.
 *
 * @property serverUrl The base URL of the Cobalt server.
 */
class Cobalt(val serverUrl: String) {
    private val raw = CobaltRaw(serverUrl)

    /**
     * Sends a Cobalt request using a custom builder with specific configuration options.
     *
     * @param builder The request builder to customize the request parameters.
     * @param setupOptions A lambda function that configures the builder with additional options.
     * @return [A wrapped Cobalt response][WrappedCobaltResponse].
     */
    suspend fun <T> request(builder: RequestBuilder<T>, setupOptions: T.() -> Unit): WrappedCobaltResponse {
        return requestWithBuilder(builder, setupOptions)
    }

    /**
     * Sends a Cobalt request using a URL and optional configuration options.
     *
     * @param url The URL for the Cobalt request.
     * @param setupOptions A lambda function that configures the request options if provided.
     * @return [A wrapped Cobalt response][WrappedCobaltResponse].
     */
    suspend fun request(
        url: String,
        setupOptions: (CobaltRequestBuilder.() -> Unit)? = null
    ): WrappedCobaltResponse = requestWithBuilder(CobaltRequestBuilder(url), setupOptions)

    /**
     * Sends a Cobalt request using a pre-built CobaltRequest object.
     *
     * @param requestObj The pre-built CobaltRequest object.
     * @return [A wrapped Cobalt response][WrappedCobaltResponse].
     */
    suspend fun request(requestObj: CobaltRequest): WrappedCobaltResponse = requestWrapped(requestObj)


    /**
     * Retrieve basic information about the Cobalt server.
     *
     * @return [CobaltServerInfo] server info
     */
    suspend fun getServerInfo() = raw.getServerInfo()

    private suspend fun <T> requestWithBuilder(
        builder: RequestBuilder<T>,
        setupOptions: (T.() -> Unit)?
    ): WrappedCobaltResponse {
        val build = build(builder, setupOptions)
        return requestWrapped(build)
    }

    private suspend fun requestWrapped(cobaltRequest: CobaltRequest): WrappedCobaltResponse {
        return raw.request(cobaltRequest).wrap()
    }

    private fun <T> build(builder: RequestBuilder<T>, setupOptions: (T.() -> Unit)? = null): CobaltRequest {
        if (setupOptions != null) {
            builder.build(setupOptions)
        }

        return builder.build()
    }
}
