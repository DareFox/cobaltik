package me.darefox.cobaltik

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal actual val RequestDispatcher: CoroutineDispatcher = Dispatchers.IO