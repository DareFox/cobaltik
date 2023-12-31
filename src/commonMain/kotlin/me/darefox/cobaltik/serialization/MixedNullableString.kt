package me.darefox.cobaltik.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import me.darefox.cobaltik.extensions.logger

/**
 * Serializer for handling nullable String values with a mixed representation of string and null as boolean/null.
 */
object MixedNullableString : KSerializer<String?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("MixedNullableString", PrimitiveKind.STRING)
    private val logger = logger {  }
    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: String?) {
        if (value != null)
            encoder.encodeString(value)
        else
            encoder.encodeNull()
    }

    override fun deserialize(decoder: Decoder): String? {
        val json = ((decoder as JsonDecoder).decodeJsonElement() as JsonPrimitive)
        return parseNull(json, decoder)
    }

    private fun parseNull(json: JsonPrimitive, decoder: Decoder): String? {
        return if (json.isString) {
            json.content
        } else {
            when (json.booleanOrNull) {
                false, null -> null
                else -> {
                    logger.error { "Json primitive ($json) is not a string, false nor null" }
                    decoder.decodeString() // create exception
                }
            }
        }
    }
}
