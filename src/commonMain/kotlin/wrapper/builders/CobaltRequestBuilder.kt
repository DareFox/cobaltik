package wrapper.builders

import models.*

class CobaltRequestBuilder(val url: String): RequestBuilder<CobaltRequestBuilder> {
    /** Applies only to YouTube downloads. h264 is recommended for phones. **/
    var videoCodec = VideoCodec.h264
    /** 720 quality is recommended for phones. **/
    var videoQuality = VideoQuality._720p
    var audioFormat = AudioFormat.mp3
    var audioOnly = false
    /** Changes whether downloaded TikTok videos have watermarks. **/
    var removeTikTokWatermark = false
    /** Disables audio track in video downloads. **/
    var muteAudio = false
    /** Backend uses Accept-Language for YouTube video audio tracks when true. **/
    var useDubLang = false
    /** Enables download of original sound used in a TikTok video. **/
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