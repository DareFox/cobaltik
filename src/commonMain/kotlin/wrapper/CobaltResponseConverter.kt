package wrapper

import models.CobaltResponse
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import models.CobaltResponseStatus as Status

/**
 * Converts a [CobaltResponse] into its corresponding [WrappedCobaltResponse] based on the response status.
 *
 * @return A [WrappedCobaltResponse] representing the converted response.
 */
@OptIn(ExperimentalContracts::class)
fun CobaltResponse.wrap(): WrappedCobaltResponse {
    return when (status) {
        Status.REDIRECT -> {
            notNull("url", url)
            RedirectResponse(url)
        }
        Status.STREAM -> {
            notNull("url", url)
            StreamResponse(url)
        }
        Status.ERROR -> ErrorResponse(text)
        Status.SUCCESS -> SuccessResponse(text)
        Status.RATELIMIT -> RateLimitResponse(text)
        Status.PICKER -> {
            notNull("picker", picker)
            notNull("pickerType", pickerType)

            PickerResponse(pickerType, picker, audio)
        }
    }

}

@ExperimentalContracts
private fun <T> notNull(name: String, value: T?): T {
    contract {
        returns() implies (value != null)
    }

    return requireNotNull(value) { "$name is null" }
}
