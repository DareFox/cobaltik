package models

import kotlinx.serialization.Serializable

@Serializable
data class CobaltError(val text: String) : Error(text)
