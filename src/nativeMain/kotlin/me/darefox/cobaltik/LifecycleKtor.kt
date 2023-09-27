package me.darefox.cobaltik

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
actual val lifecycleThread: CoroutineContext = newSingleThreadContext("Cobaltik-KtorLifecycle")
