package extensions

import models.CobaltError
import wrapper.ErrorResponse
import wrapper.WrappedCobaltResponse

fun WrappedCobaltResponse.throwIfError(): WrappedCobaltResponse {
    if (this is ErrorResponse) {
        throw CobaltError(text ?: "[No error text]")
    }

    return this
}