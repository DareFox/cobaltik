package wrapper

import CobaltRaw
import models.CobaltRequest
import wrapper.builders.AudioRequestBuilder
import wrapper.builders.CobaltRequestBuilder
import wrapper.builders.RequestBuilder

class Cobalt(val serverUrl: String) {
    private val raw = CobaltRaw(serverUrl)

    suspend fun requestAudio(
        url: String,
        setupOptions: (AudioRequestBuilder.() -> Unit)? = null
    ): WrappedCobaltResponse = requestWithBuilder(AudioRequestBuilder(url), setupOptions)

    suspend fun request(
        url: String,
        setupOptions: (CobaltRequestBuilder.() -> Unit)? = null
    ): WrappedCobaltResponse = requestWithBuilder(CobaltRequestBuilder(url), setupOptions)

    private suspend fun <T> requestWithBuilder(builder: RequestBuilder<T>, setupOptions: (T.() -> Unit)?): WrappedCobaltResponse {
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