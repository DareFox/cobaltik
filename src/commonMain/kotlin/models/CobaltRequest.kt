package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CobaltRequest(
    @SerialName("url")
    val url: String,
    /** Applies only to YouTube downloads. h264 is recommended for phones. **/
    @SerialName("vCodec")
    val videoCodec: VideoCodec = VideoCodec.h264,
    @SerialName("vQuality")
    val videoQuality: VideoQuality = VideoQuality.P720,
    @SerialName("aFormat")
    val audioFormat: AudioFormat = AudioFormat.MP3,
    @SerialName("isAudioOnly")
    val isAudioOnly: Boolean = false,
    @SerialName("isNoTTWatermark")
    val removeTikTokWatermark: Boolean = true,
    /** Enables download of original sound used in a TikTok video. **/
    @SerialName("isTTFullAudio")
    val downloadFullTikTokAudio: Boolean = false,
    /** Disables audio track in video downloads. **/
    @SerialName("isAudioMuted")
    val isAudioMuted: Boolean = false,
    @SerialName("dubLang")
    val useDubLang: Boolean = false
)

