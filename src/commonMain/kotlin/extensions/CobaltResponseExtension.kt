package extensions

import models.CobaltError
import models.CobaltResponse
import models.CobaltResponseStatus

fun CobaltResponse.isError(): Boolean {
    return status == CobaltResponseStatus.ERROR
}

fun CobaltResponse.throwIfError(): CobaltResponse {
    if (isError()) {
        throw CobaltError(text ?: "[No error text]")
    }

    return this
}