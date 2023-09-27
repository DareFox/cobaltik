package wrapper.builders

import models.CobaltRequest
import models.VideoQuality

/**
 * The `VideoRequestBuilder` class is used to construct Cobalt generic video requests with specific configuration options.
 *
 * @param url The URL for the video request.
 *
 * @property videoQuality
 * @property muteAudio
 *
 * @see YouTubeRequestBuilder For creating specific YouTube requests
 * @see TikTokRequestBuilder For creating specific TikTok requests
 */
class VideoRequestBuilder(val url: String): RequestBuilder<VideoRequestBuilder> {
    /** The video quality to use for the request. 720 quality is recommended for phones. Defaults to [VideoQuality.P720]  **/
    var videoQuality = VideoQuality.P720
    /** Disables audio track in video downloads. Defaults to `false` **/
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