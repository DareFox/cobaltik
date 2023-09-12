import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
actual val lifecycleThread: CoroutineContext = newSingleThreadContext("Cobaltik-KtorLifecycle")