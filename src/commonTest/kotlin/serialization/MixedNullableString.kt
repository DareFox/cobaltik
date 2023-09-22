import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.Serializable
import serialization.MixedNullableString
import kotlin.test.assertFails

@Serializable
data class TestJson(
    @Serializable(with = MixedNullableString::class)
    val field: String?
)

class MixedNullableStringTest {

    @Test
    fun testSerializationAndDeserialization() {
        // Create an instance of TestJson
        val testJson = TestJson(field = "Hello")

        // Serialize the object to JSON
        val jsonStr = Json.encodeToString(testJson)

        // Deserialize the JSON back to an object
        val deserializedJson = Json.decodeFromString<TestJson>(jsonStr)

        // Assert that the deserialized object is equal to the original
        assertEquals(testJson, deserializedJson)
    }

    @Test
    fun testSerializationAndDeserializationWithNull() {
        // Create an instance of TestJson with a null field
        val testJson = TestJson(field = null)

        // Serialize the object to JSON
        val jsonStr = Json.encodeToString(testJson)

        // Deserialize the JSON back to an object
        val deserializedJson = Json.decodeFromString<TestJson>(jsonStr)

        // Assert that the deserialized object is equal to the original
        assertEquals(testJson, deserializedJson)
    }

    @Test
    fun testDeserializationWithFalse() {
        // Deserialize the JSON back to an object
        val deserializedJson = Json.decodeFromString<TestJson>("""
            { "field": false }
        """.trimIndent())

        // Assert that the deserialized false is null
        assertEquals(TestJson(null), deserializedJson)
    }

    @Test
    fun testDeserializationWithTrue() {
        assertFails {
            Json.decodeFromString<TestJson>("""
                { "field": true }
            """.trimIndent())
        }
    }
}
