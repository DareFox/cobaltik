package wrapper.builders

import models.AudioFormat
import models.CobaltRequest

/**
 * The `AudioRequestBuilder` class provides a builder for configuring audio only Cobalt requests.
 *
 * @param url The URL for the audio request.
 *
 * @property audioFormat
 * @property useDubLang
 */
class AudioRequestBuilder(val url: String): RequestBuilder<AudioRequestBuilder> {
    /** The audio format to use for the request. Defaults to [AudioFormat.mp3]. **/
    var audioFormat = AudioFormat.mp3
    /** Backend uses Accept-Language for YouTube video audio tracks when true. Defaults to `false` **/
    var useDubLang = false

    override fun build() = CobaltRequestBuilder(url).build {
        audioOnly = true
        audioFormat = this@AudioRequestBuilder.audioFormat
        useDubLang = this@AudioRequestBuilder.useDubLang
    }

    override fun build(func: AudioRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}