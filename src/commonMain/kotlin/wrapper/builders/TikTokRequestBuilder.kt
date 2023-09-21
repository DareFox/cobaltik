package wrapper.builders

import models.CobaltRequest
import models.VideoCodec
import models.VideoQuality

/**
 * Builder class for constructing TikTok video download requests with customizable options.
 *
 * @param url The URL of the TikTok video.
 *
 * @property downloadFullTikTokAudio
 * @property videoQuality
 * @property muteAudio
 * @property removeTikTokWatermark
 * @property audioOnly
 */
class TikTokRequestBuilder(val url: String): RequestBuilder<TikTokRequestBuilder> {
    /** Enables download of original sound used in a TikTok video. Defaults to `false` **/
    var downloadFullTikTokAudio = false
    /** The video quality to use for the request. 720 quality is recommended for phones. Defaults to [VideoQuality._720p]  **/
    var videoQuality = VideoQuality._720p
    /** Disables audio track in video downloads. Defaults to `false` **/
    var muteAudio = false
    /** Changes whether downloaded TikTok videos have watermarks. Defaults to `false` **/
    var removeTikTokWatermark = false
    /** Determines whether the request should return only audio. Defaults to `false`. **/
    var audioOnly = false

    override fun build() = CobaltRequestBuilder(url).build {
        videoQuality = this@TikTokRequestBuilder.videoQuality
        muteAudio = this@TikTokRequestBuilder.muteAudio
        removeTikTokWatermark = this@TikTokRequestBuilder.removeTikTokWatermark
        downloadFullTikTokAudio = this@TikTokRequestBuilder.downloadFullTikTokAudio
        audioOnly = this@TikTokRequestBuilder.audioOnly
    }

    override fun build(func: TikTokRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}