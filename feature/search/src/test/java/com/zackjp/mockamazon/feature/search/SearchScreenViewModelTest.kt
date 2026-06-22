package com.zackjp.mockamazon.feature.search

import app.cash.turbine.test
import com.zackjp.mockamazon.shared.testutils.SetMainCoroutineDispatcher
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(SetMainCoroutineDispatcher::class)
class SearchScreenViewModelTest {

    private val fakeRepository = FakeSearchHistoryRepository()
    private val viewModel = SearchScreenViewModel(fakeRepository)

    @Test
    fun searchItems_InitiallyEmpty() = runTest {
        viewModel.searchItems.test {
            awaitItem() shouldBe emptyList()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchItems_EmitsNonEmptyHistoryFromRepository() = runTest {
        fakeRepository.saveQuery("popcorn")

        viewModel.searchItems.test {
            awaitItem() shouldBe emptyList()
            awaitItem() shouldBe listOf("popcorn")
        }
    }
}
