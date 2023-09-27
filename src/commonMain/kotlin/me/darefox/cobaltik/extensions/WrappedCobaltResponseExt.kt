package me.darefox.cobaltik.extensions

import me.darefox.cobaltik.models.CobaltError
import me.darefox.cobaltik.wrapper.ErrorResponse
import me.darefox.cobaltik.wrapper.WrappedCobaltResponse

/**
 * Throws a [CobaltError] exception if this [WrappedCobaltResponse] represents [an error response][ErrorResponse].
 * If the response is not an error, it returns the response itself.
 *
 * @return This [WrappedCobaltResponse] if it's not an [ErrorResponse]
 * @throws CobaltError if wrapped response is [ErrorResponse].
 */
fun WrappedCobaltResponse.throwIfError(): WrappedCobaltResponse {
    if (this is ErrorResponse) {
        throw CobaltError(text ?: "[No error text]")
    }

    return this
}
