import extensions.logger
import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

internal expect val lifecycleThread: CoroutineContext

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

    suspend fun <T> use(func: suspend (HttpClient) -> T): T {
        activeUses.getAndUpdate { it + 1 }
        try {
            val client = getClientSafely()
            return func(client)
        } finally { // coroutine cancellation
            activeUses.getAndUpdate { it - 1 }
        }

    }

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

    private fun killKtor() {
        logger.debug { "Killing ktor for inactivity" }
        activeClient?.close()
        activeClient = null
    }

    private fun updateTimer() {
        logger.debug { "Starting timer because ktor have 0 active usages" }
        timer?.cancel("Restart timer")
        timer = scope.launch {
            delay(inactivityDuration)
            killKtor()
        }
    }
}