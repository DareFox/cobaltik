package me.darefox.cobaltik.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import me.darefox.cobaltik.RequestDispatcher

internal suspend inline fun <T> withRequestDispatcher(noinline block: suspend CoroutineScope.() -> T): T {
    return withContext(RequestDispatcher, block)
}