package wrapper.builders

import models.AudioFormat
import models.CobaltRequest

class AudioRequestBuilder(val url: String): RequestBuilder<AudioRequestBuilder> {
    var audioFormat = AudioFormat.mp3
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