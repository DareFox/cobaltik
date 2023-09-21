package wrapper

import kotlinx.serialization.Serializable
import models.PickerItem
import models.PickerType

@Serializable
sealed class WrappedCobaltResponse

@Serializable
data class ErrorResponse(val text: String?): WrappedCobaltResponse()
@Serializable
data class RateLimitResponse(val text: String?): WrappedCobaltResponse()
@Serializable
data class SuccessResponse(val text: String?): WrappedCobaltResponse()
@Serializable
data class RedirectResponse(val redirectUrl: String): WrappedCobaltResponse()
@Serializable
data class StreamResponse(val streamUrl: String): WrappedCobaltResponse()
@Serializable
data class PickerResponse(
    val type: PickerType,
    val items: List<PickerItem>,
    val audioUrl: String?
): WrappedCobaltResponse()