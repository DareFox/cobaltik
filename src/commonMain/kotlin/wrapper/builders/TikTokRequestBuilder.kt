package wrapper.builders

import models.CobaltRequest
import models.VideoCodec
import models.VideoQuality

class TikTokRequestBuilder(val url: String) {
    var downloadFullTikTokAudio = false
    var videoQuality = VideoQuality._720p
    var muteAudio = false
    var removeTikTokWatermark = false

    fun build() = CobaltRequestBuilder(url).build {
        videoQuality = this@TikTokRequestBuilder.videoQuality
        muteAudio = this@TikTokRequestBuilder.muteAudio
        removeTikTokWatermark = this@TikTokRequestBuilder.removeTikTokWatermark
        downloadFullTikTokAudio = this@TikTokRequestBuilder.downloadFullTikTokAudio
    }

    fun build(func: TikTokRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}