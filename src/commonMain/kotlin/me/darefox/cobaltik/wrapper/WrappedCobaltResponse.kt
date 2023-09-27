package me.darefox.cobaltik.wrapper

import kotlinx.serialization.Serializable
import me.darefox.cobaltik.models.PickerItem
import me.darefox.cobaltik.models.PickerType

/**
 * Represents a sealed class for WrappedCobaltResponse, which can be one of the following response types.
 *
 * @see ErrorResponse
 * @see RateLimitResponse
 * @see SuccessResponse
 * @see RedirectResponse
 * @see StreamResponse
 * @see PickerResponse
 */
@Serializable
sealed class WrappedCobaltResponse

/**
 * Represents an error response containing an optional [text] message.
 *
 * @param text The optional text message describing the error.
 */
@Serializable
data class ErrorResponse(val text: String?) : WrappedCobaltResponse()

/**
 * Represents a rate limit response containing an optional [text] message.
 *
 * @param text The optional text message indicating a rate limit.
 */
@Serializable
data class RateLimitResponse(val text: String?) : WrappedCobaltResponse()

/**
 * Represents a success response containing an optional [text] message.
 *
 * @param text The optional text message indicating a successful response.
 */
@Serializable
data class SuccessResponse(val text: String?) : WrappedCobaltResponse()

/**
 * Represents a redirect response containing the [redirectUrl].
 *
 * @param redirectUrl The URL to which the response redirects.
 */
@Serializable
data class RedirectResponse(val redirectUrl: String) : WrappedCobaltResponse()

/**
 * Represents a stream response containing the [streamUrl].
 *
 * @param streamUrl The URL of the stream.
 */
@Serializable
data class StreamResponse(val streamUrl: String) : WrappedCobaltResponse()

/**
 * Represents a picker (a.k.a gallery) response with
 * the specified [type], a list of [items], and an optional [audioUrl].
 *
 * @param type The type of the picker response.
 * @param items The list of picker items.
 * @param audioUrl The optional audio URL associated with the response.
 */
@Serializable
data class PickerResponse(
    val type: PickerType,
    val items: List<PickerItem>,
    val audioUrl: String?
) : WrappedCobaltResponse()
