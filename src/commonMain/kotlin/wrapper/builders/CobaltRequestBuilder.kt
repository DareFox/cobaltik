package wrapper.builders

import models.*

/**
 * A builder class for creating CobaltRequest instances with various configuration options.
 *
 * @param url The URL for the Cobalt request.
 *
 * @property videoCodec
 * @property videoQuality
 * @property audioFormat
 * @property audioOnly
 * @property removeTikTokWatermark
 * @property muteAudio
 * @property useDubLang
 * @property downloadFullTikTokAudio
 */
class CobaltRequestBuilder(val url: String): RequestBuilder<CobaltRequestBuilder> {
    /** Applies only to YouTube downloads. h264 is recommended for phones. Defaults to [VideoCodec.h264] **/
    var videoCodec = VideoCodec.h264
    /** The video quality to use for the request. 720 quality is recommended for phones. Defaults to [VideoQuality.P720]  **/
    var videoQuality = VideoQuality.P720
    /** The audio format to use for the request. Defaults to [AudioFormat.mp3]. **/
    var audioFormat = AudioFormat.mp3
    /** Determines whether the request should return only audio. Defaults to `false`. **/
    var audioOnly = false
    /** Changes whether downloaded TikTok videos have watermarks. Defaults to `false` **/
    var removeTikTokWatermark = false
    /** Disables audio track in video downloads. Defaults to `false` **/
    var muteAudio = false
    /** Backend uses Accept-Language for YouTube video audio tracks when true. Defaults to `false` **/
    var useDubLang = false
    /** Enables download of original sound used in a TikTok video. Defaults to `false` **/
    var downloadFullTikTokAudio = false

    override fun build() = CobaltRequest(
        url = this.url,
        videoCodec = this.videoCodec,
        videoQuality = this.videoQuality,
        audioFormat = this.audioFormat,
        isAudioOnly = this.audioOnly,
        removeTikTokWatermark = this.removeTikTokWatermark,
        downloadFullTikTokAudio = this.downloadFullTikTokAudio,
        isAudioMuted = this.muteAudio,
        useDubLang = this.useDubLang
    )

    override fun build(func: CobaltRequestBuilder.() -> Unit): CobaltRequest {
        this.func()
        return this.build()
    }
}