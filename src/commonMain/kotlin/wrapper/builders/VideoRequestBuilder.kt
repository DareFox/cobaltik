package wrapper.builders

import models.AudioFormat
import models.CobaltRequest
import models.VideoCodec
import models.VideoQuality

class VideoRequestBuilder(val url: String) {
    var videoQuality = VideoQuality._720p
    var muteAudio = false

    fun build() = CobaltRequestBuilder(url).build {
        videoQuality = this@VideoRequestBuilder.videoQuality
        muteAudio = this@VideoRequestBuilder.muteAudio
    }

    fun build(func: VideoRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}