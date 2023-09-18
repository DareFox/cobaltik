package wrapper.builders

import models.*

class CobaltRequestBuilder(val url: String): RequestBuilder<CobaltRequestBuilder> {
    var videoCodec = VideoCodec.h264
    var videoQuality = VideoQuality._720p
    var audioFormat = AudioFormat.mp3
    var audioOnly = false
    val removeTikTokWatermark = false
    var muteAudio = false
    var useDubLang = false

    override fun build() = CobaltRequest(
        url = this.url,
        videoCodec = this.videoCodec,
        videoQuality = this.videoQuality,
        audioFormat = this.audioFormat,
        isAudioOnly = this.audioOnly,
        removeTikTokWatermark = this.removeTikTokWatermark,
        isAudioMuted = this.muteAudio,
        useDubLang = this.useDubLang
    )

    override fun build(func: CobaltRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}