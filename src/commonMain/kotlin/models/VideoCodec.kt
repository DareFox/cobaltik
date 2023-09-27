package models

import kotlinx.serialization.Serializable

@Serializable
enum class VideoCodec {
    H264,
    AV1,
    VP9
}
