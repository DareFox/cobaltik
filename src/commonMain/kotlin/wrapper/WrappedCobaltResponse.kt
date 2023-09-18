package wrapper

import kotlinx.serialization.Serializable
import models.PickerItem
import models.PickerType

@Serializable
sealed class WrappedCobaltResponse

data class ErrorResponse(val text: String?): WrappedCobaltResponse()
data class RateLimitResponse(val text: String?): WrappedCobaltResponse()
data class SuccessResponse(val text: String?): WrappedCobaltResponse()
data class RedirectResponse(val redirectUrl: String): WrappedCobaltResponse()
data class StreamResponse(val streamUrl: String): WrappedCobaltResponse()
data class PickerResponse(
    val type: PickerType,
    val items: List<PickerItem>,
    val audioUrl: String?
): WrappedCobaltResponse()