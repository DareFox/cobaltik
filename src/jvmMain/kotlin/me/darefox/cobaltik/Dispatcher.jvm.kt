package me.darefox.cobaltik

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual val RequestDispatcher: CoroutineDispatcher = Dispatchers.IO