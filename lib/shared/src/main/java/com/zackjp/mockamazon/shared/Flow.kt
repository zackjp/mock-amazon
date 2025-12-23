package com.zackjp.mockamazon.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


inline fun <reified T> MutableStateFlow<in T>.updateIf(updateBlock: (T) -> T) {
    update { current ->
        if (current is T) updateBlock(current) else current
    }
}
