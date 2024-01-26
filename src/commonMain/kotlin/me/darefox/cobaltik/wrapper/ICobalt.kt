package me.darefox.cobaltik.wrapper

import me.darefox.cobaltik.models.CobaltRequest
import me.darefox.cobaltik.models.CobaltServerInfo
import me.darefox.cobaltik.wrapper.builders.CobaltRequestBuilder
import me.darefox.cobaltik.wrapper.builders.RequestBuilder

interface ICobalt {
    val serverUrl: String

    /**
     * Sends a Cobalt request using a custom builder with specific configuration options.
     *
     * @param builder The request builder to customize the request parameters.
     * @param setupOptions A lambda function that configures the builder with additional options.
     * @return [A wrapped Cobalt response][WrappedCobaltResponse].
     */
    suspend fun <T> request(builder: RequestBuilder<T>, setupOptions: T.() -> Unit): WrappedCobaltResponse

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
    ): WrappedCobaltResponse

    /**
     * Sends a Cobalt request using a pre-built CobaltRequest object.
     *
     * @param requestObj The pre-built CobaltRequest object.
     * @return [A wrapped Cobalt response][WrappedCobaltResponse].
     */
    suspend fun request(requestObj: CobaltRequest): WrappedCobaltResponse

    /**
     * Retrieve basic information about the Cobalt server.
     *
     * @return [CobaltServerInfo] server info
     */
    suspend fun getServerInfo(): CobaltServerInfo
}