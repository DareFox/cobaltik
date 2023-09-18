package wrapper

import models.CobaltResponse
import models.CobaltResponseStatus.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun CobaltResponse.wrap(): WrappedCobaltResponse {
    return when (status) {
        ERROR -> {
            notNull("url", url)
            ErrorResponse(text)
        }
        REDIRECT -> {
            notNull("url", url)
            RedirectResponse(url)
        }
        STREAM -> {
            notNull("url", url)
            StreamResponse(url)
        }
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