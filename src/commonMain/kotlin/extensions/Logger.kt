package extensions

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * Creates a Kotlin logger using [KotlinLogging]
 *
 * @param func A lambda function that represents the context for the logger.
 * @return A [KLogger] instance associated with the provided function's context.
 */
internal fun logger(func: () -> Unit): KLogger {
    return KotlinLogging.logger(func)
}
