package com.example.fakeamazon.base

import kotlinx.coroutines.Dispatchers
import org.junit.Test

class DefaultDispatcherProviderTest {

    @Test
    fun init_DispatchersAreCorrect() {
        val provider = DefaultDispatcherProvider()

        assert(provider.default == Dispatchers.Default)
        assert(provider.io == Dispatchers.IO)
        assert(provider.main == Dispatchers.Main)
    }

}
