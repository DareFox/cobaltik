package models

import kotlinx.serialization.Serializable

@Serializable
enum class AudioFormat {
    BEST,
    MP3,
    OGG,
    WAV
}
