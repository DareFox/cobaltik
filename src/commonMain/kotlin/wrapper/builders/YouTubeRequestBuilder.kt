package wrapper.builders

import models.CobaltRequest
import models.VideoCodec
import models.VideoQuality

class YouTubeRequestBuilder(val url: String): RequestBuilder<YouTubeRequestBuilder> {
    var videoQuality = VideoQuality._720p
    var videoCodec = VideoCodec.h264
    var muteAudio = false
    var removeTikTokWatermark = false
    var useDubLang = false

    override fun build() = CobaltRequestBuilder(url).build {
        videoQuality = this@YouTubeRequestBuilder.videoQuality
        videoCodec = this@YouTubeRequestBuilder.videoCodec
        muteAudio = this@YouTubeRequestBuilder.muteAudio
        removeTikTokWatermark = this@YouTubeRequestBuilder.removeTikTokWatermark
        useDubLang = this@YouTubeRequestBuilder.useDubLang
    }

    override fun build(func: YouTubeRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}