package models

import kotlinx.serialization.Serializable
import serialization.MixedNullableString

@Serializable
data class CobaltResponse(
    val status: CobaltResponseStatus,
    val url: String? = null,
    val text: String? = null,
    val pickerType: PickerType? = null,
    val picker: List<PickerItem>? = null,
    @Serializable(with = MixedNullableString::class)
    val audio: String? = null
)

