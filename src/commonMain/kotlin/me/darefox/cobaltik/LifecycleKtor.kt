package me.darefox.cobaltik

import me.darefox.cobaltik.extensions.logger
import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

internal expect val lifecycleThread: CoroutineContext

/**
 * A utility class that manages the lifecycle of a Ktor HTTP client that automatically
 * shutdown after a period of inactivity.
 *
 * @param inactivityDuration The duration of inactivity after which the Ktor client should be
 * shut down.
 * @param ktorBuilder A function that constructs a new instance of the Ktor HTTP client.
 */
internal class LifecycleKtor(
    private val inactivityDuration: Duration,
    private val ktorBuilder: () -> HttpClient
) {
    private val logger = logger {  }
    private var activeClient: HttpClient? = null
    private val activeUses = MutableStateFlow(0)

    private val scope = CoroutineScope(lifecycleThread)
    private val jobUpdater: Job = scope.launch {
        activeUses.collect { uses ->
            if (uses == 0) {
                updateTimer()
            } else {
                timer?.let {
                    logger.debug { "Cancelling timer because activeUses is not 0" }
                    it.cancel()
                }
                timer = null
            }
        }
    }
    private var timer: Job? = null

    /**
     * Suspends the current coroutine and uses the Ktor HTTP client for a specific operation.
     *
     * @param func The suspending function that uses the Ktor HTTP client.
     * @return The result of the operation.
     */

    suspend fun <T> use(func: suspend (HttpClient) -> T): T {
        activeUses.getAndUpdate { it + 1 }
        try {
            val client = getClientSafely()
            return func(client)
        } finally { // coroutine cancellation
            activeUses.getAndUpdate { it - 1 }
        }
    }

    /**
     * Safely retrieves the Ktor HTTP client, creating a new one if necessary.
     *
     * @return The Ktor HTTP client.
     */
    private suspend fun getClientSafely(): HttpClient {
        return scope.async {
            activeClient ?: run {
                logger.debug { "Creating Ktor client" }
                val client = ktorBuilder()
                activeClient = client
                client
            }
        }.await()
    }

    /**
     * Shuts down the Ktor HTTP client due to inactivity.
     */
    private fun killKtor() {
        logger.debug { "Killing ktor for inactivity" }
        activeClient?.close()
        activeClient = null
    }

    /**
     * Updates the inactivity timer, restarting it.
     */
    private fun updateTimer() {
        logger.debug { "Starting timer because ktor have 0 active usages" }
        timer?.cancel("Restart timer")
        timer = scope.launch {
            delay(inactivityDuration)
            killKtor()
        }
    }
}
