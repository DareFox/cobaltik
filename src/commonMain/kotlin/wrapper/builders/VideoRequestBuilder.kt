package wrapper.builders

import models.AudioFormat
import models.CobaltRequest
import models.VideoCodec
import models.VideoQuality

class VideoRequestBuilder(val url: String): RequestBuilder<VideoRequestBuilder> {
    var videoQuality = VideoQuality._720p
    var muteAudio = false

    override fun build() = CobaltRequestBuilder(url).build {
        videoQuality = this@VideoRequestBuilder.videoQuality
        muteAudio = this@VideoRequestBuilder.muteAudio
    }

    override fun build(func: VideoRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}