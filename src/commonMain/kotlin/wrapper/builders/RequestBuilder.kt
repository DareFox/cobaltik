package wrapper.builders

import models.CobaltRequest

interface RequestBuilder<T> {
    fun build(): CobaltRequest
    fun build(func: T.() -> Unit): CobaltRequest
}