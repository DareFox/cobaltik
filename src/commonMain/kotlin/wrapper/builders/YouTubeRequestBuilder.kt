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
 * @property audioOnly
 */
class YouTubeRequestBuilder(val url: String): RequestBuilder<YouTubeRequestBuilder> {
    /** The video quality to use for the request. 720 quality is recommended for phones. Defaults to [VideoQuality.P720]  **/
    var videoQuality = VideoQuality.P720
    /** Applies only to YouTube downloads. h264 is recommended for phones. Defaults to [VideoCodec.h264] **/
    var videoCodec = VideoCodec.h264
    /** Disables audio track in video downloads. Defaults to `false` **/
    var muteAudio = false
    /** Backend uses Accept-Language for YouTube video audio tracks when true. Defaults to `false` **/
    var useDubLang = false
    /** Determines whether the request should return only audio. Defaults to `false`. **/
    var audioOnly = false

    override fun build() = CobaltRequestBuilder(url).build {
        videoQuality = this@YouTubeRequestBuilder.videoQuality
        videoCodec = this@YouTubeRequestBuilder.videoCodec
        muteAudio = this@YouTubeRequestBuilder.muteAudio
        useDubLang = this@YouTubeRequestBuilder.useDubLang
        audioOnly = this@YouTubeRequestBuilder.audioOnly
    }

    override fun build(func: YouTubeRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}