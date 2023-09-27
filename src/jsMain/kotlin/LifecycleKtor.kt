import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val lifecycleThread: CoroutineContext = Dispatchers.Default
