package com.zackjp.mockamazon.shared.testutils

import com.zackjp.mockamazon.shared.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatcherProvider(
    dispatcher: CoroutineDispatcher = StandardTestDispatcher()
) : DispatcherProvider {
    override val default: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val main: CoroutineDispatcher = dispatcher
}
