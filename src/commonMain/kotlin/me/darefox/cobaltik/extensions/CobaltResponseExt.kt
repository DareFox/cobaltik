package me.darefox.cobaltik.extensions

import me.darefox.cobaltik.models.CobaltError
import me.darefox.cobaltik.models.CobaltResponse
import me.darefox.cobaltik.models.CobaltResponseStatus

/**
 * Checks if this [CobaltResponse] represents an error response.
 *
 * @return `true` if this response is an error; `false` otherwise.
 */
fun CobaltResponse.isError(): Boolean {
    return status == CobaltResponseStatus.ERROR
}

/**
 * Throws a [CobaltError] exception if this [CobaltResponse] represents an error response.
 * If the response is not an error, it returns the response itself.
 *
 * @return This [CobaltResponse] if response isn't error
 * @throws CobaltError if response is error.
 */
fun CobaltResponse.throwIfError(): CobaltResponse {
    if (isError()) {
        throw CobaltError(text ?: "[No error text]")
    }

    return this
}
