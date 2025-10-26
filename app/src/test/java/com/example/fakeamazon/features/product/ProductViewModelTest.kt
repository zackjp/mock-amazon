package com.example.fakeamazon.features.product

import com.example.fakeamazon.base.TestDispatcherProvider
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class) // advanceUntilIdle()
class ProductViewModelTest {

    val testDispatcherProvider = TestDispatcherProvider()
    val dispatcher = testDispatcherProvider.default
    val scheduler = dispatcher.testCoroutineScheduler

    val viewModel = ProductViewModel(testDispatcherProvider)

    @Test
    fun viewModel_Init_DoesNotLoad() = runTest(dispatcher) {
        scheduler.advanceUntilIdle()
        viewModel.productInfo.first() shouldBe null
    }

    @Test
    fun viewModel_Load_LoadsProductInfoAsync() = runTest(dispatcher) {
        viewModel.load()

        viewModel.productInfo.first() shouldBe null
        scheduler.advanceUntilIdle()
        viewModel.productInfo.first() shouldNotBe null
    }
}