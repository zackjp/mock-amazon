package com.example.mockamazon.shared

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.Test

class DefaultDispatcherProviderTest {

    @Test
    fun init_DispatchersAreCorrect() {
        val provider = DefaultDispatcherProvider()

        provider.default shouldBe Dispatchers.Default
        provider.io shouldBe Dispatchers.IO
        provider.main shouldBe  Dispatchers.Main
    }

}
