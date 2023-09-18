package wrapper.builders

import models.*

class CobaltRequestBuilder(var url: String) {
    var videoCodec = VideoCodec.h264
    var videoQuality = VideoQuality._720p
    var audioFormat = AudioFormat.mp3
    var audioOnly = false
    val removeTikTokWatermark = false
    var muteAudio = false
    var useDubLang = false

    fun build() = CobaltRequest(
        url = this.url,
        videoCodec = this.videoCodec,
        videoQuality = this.videoQuality,
        audioFormat = this.audioFormat,
        isAudioOnly = this.audioOnly,
        removeTikTokWatermark = this.removeTikTokWatermark,
        isAudioMuted = this.muteAudio,
        useDubLang = this.useDubLang
    )

    fun build(func: CobaltRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}