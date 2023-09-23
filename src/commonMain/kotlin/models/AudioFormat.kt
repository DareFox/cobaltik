package models

import kotlinx.serialization.Serializable

@Serializable
enum class AudioFormat {
    mp3,
    best,
    ogg,
    wav
}