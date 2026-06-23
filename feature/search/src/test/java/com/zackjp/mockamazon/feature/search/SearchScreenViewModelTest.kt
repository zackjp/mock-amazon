package com.zackjp.mockamazon.feature.search

import app.cash.turbine.test
import com.zackjp.mockamazon.shared.testutils.SetMainCoroutineDispatcher
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(SetMainCoroutineDispatcher::class)
class SearchScreenViewModelTest {

    private val historyFlow = MutableStateFlow(emptyList<String>())
    private val repository = mockk<SearchHistoryRepository>(relaxed = true) {
        every { observeHistory() } returns historyFlow
    }
    private val viewModel = SearchScreenViewModel(repository)

    @Test
    fun searchItems_InitiallyEmpty() = runTest {
        viewModel.searchItems.test {
            awaitItem() shouldBe emptyList()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchItems_EmitsHistoryFromRepository() = runTest {
        viewModel.searchItems.test {
            awaitItem() shouldBe emptyList()

            historyFlow.value = listOf("query1")

            awaitItem() shouldBe listOf("query1")
        }
    }

}
