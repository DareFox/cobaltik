package me.darefox.cobaltik.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class VideoQuality {
    @SerialName("144")
    P144,

    @SerialName("240")
    P240,

    @SerialName("360")
    P360,

    @SerialName("480")
    P480,

    @SerialName("720")
    P720,

    @SerialName("1080")
    P1080,

    @SerialName("1440")
    P1440,

    @SerialName("2160")
    P2160,

    @SerialName("max")
    MAX,
}
