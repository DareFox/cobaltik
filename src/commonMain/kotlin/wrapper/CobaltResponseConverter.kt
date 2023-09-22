package wrapper

import models.CobaltResponse
import models.CobaltResponseStatus.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Converts a [CobaltResponse] into its corresponding [WrappedCobaltResponse] based on the response status.
 *
 * @return A [WrappedCobaltResponse] representing the converted response.
 */
@OptIn(ExperimentalContracts::class)
fun CobaltResponse.wrap(): WrappedCobaltResponse {
    return when (status) {
        REDIRECT -> {
            notNull("url", url)
            RedirectResponse(url)
        }
        STREAM -> {
            notNull("url", url)
            StreamResponse(url)
        }
        ERROR -> ErrorResponse(text)
        SUCCESS -> SuccessResponse(text)
        RATELIMIT -> RateLimitResponse(text)
        PICKER -> {
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