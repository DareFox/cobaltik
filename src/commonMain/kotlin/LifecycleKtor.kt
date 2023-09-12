import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.datetime.Clock
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

expect val lifecycleThread: CoroutineContext

class LifecycleKtor(
    private val inactivityDuration: Duration,
    private val ktorBuilder: () -> HttpClient
) {
    private var activeClient: HttpClient? = null
    private val activeUses = MutableStateFlow(0)
    private val scope = CoroutineScope(lifecycleThread)
    private val jobUpdater: Job = scope.launch {
        activeUses.collect {
            if (it == 0) {
                println("0 connections")
                updateTimer()
            } else {
                println("New active usage, cancel timer")
                timer?.cancel("New active usage")
            }
        }
    }
    private var timer: Job? = null

    suspend fun use(func: suspend (HttpClient) -> Unit) {
        val client = getClientSafely()

        activeUses.getAndUpdate { it + 1 }
        try {
            func(client)
        } finally {
            activeUses.getAndUpdate { it - 1 }
        }
    }

    private suspend fun getClientSafely(): HttpClient {
        return scope.async {
            activeClient ?: run {
                val client = ktorBuilder()
                activeClient = client
                client
            }
        }.await()
    }

    private fun killKtor() {
        println("Kill Ktor")
        activeClient?.close()
        activeClient = null
    }

    private fun updateTimer() {
        timer?.cancel("Restart timer")
        timer = scope.launch {
            delay(inactivityDuration)
            killKtor()
        }
    }
}