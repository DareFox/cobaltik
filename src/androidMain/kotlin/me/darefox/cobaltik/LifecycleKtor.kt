package me.darefox.cobaltik

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

@OptIn(DelicateCoroutinesApi::class)
actual val lifecycleThread: CoroutineContext = newSingleThreadContext("Cobaltik-KtorLifecycle")
