package wrapper.builders

import models.CobaltRequest
import models.VideoCodec
import models.VideoQuality

/**
 * A builder class for constructing YouTube-specific Cobalt requests with customizable options.
 *
 * @param url The URL of the YouTube video to request.
 *
 * @property videoQuality
 * @property videoCodec
 * @property muteAudio
 * @property useDubLang
 */
class YouTubeRequestBuilder(val url: String): RequestBuilder<YouTubeRequestBuilder> {
    /** The video quality to use for the request. 720 quality is recommended for phones. Defaults to [VideoQuality._720p]  **/
    var videoQuality = VideoQuality._720p
    /** Applies only to YouTube downloads. h264 is recommended for phones. Defaults to [VideoCodec.h264] **/
    var videoCodec = VideoCodec.h264
    /** Disables audio track in video downloads. Defaults to `false` **/
    var muteAudio = false
    /** Backend uses Accept-Language for YouTube video audio tracks when true. Defaults to `false` **/
    var useDubLang = false

    override fun build() = CobaltRequestBuilder(url).build {
        videoQuality = this@YouTubeRequestBuilder.videoQuality
        videoCodec = this@YouTubeRequestBuilder.videoCodec
        muteAudio = this@YouTubeRequestBuilder.muteAudio
        useDubLang = this@YouTubeRequestBuilder.useDubLang
    }

    override fun build(func: YouTubeRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}