package com.zackjp.mockamazon.app.ui.view

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import app.cash.turbine.test
import com.zackjp.mockamazon.feature.search.SearchHistoryRepository
import com.zackjp.mockamazon.shared.testutils.SetMainCoroutineDispatcher
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(SetMainCoroutineDispatcher::class)
class GlobalSearchBarViewModelTest {

    private val repository = mockk<SearchHistoryRepository>(relaxed = true)
    private val viewModel = GlobalSearchBarViewModel(repository)

    @Test
    fun updateSearchText_EmitsNewTextValue() = runTest {
        val newText = "new search text"
        viewModel.searchText.test {
            viewModel.updateSearchText(TextFieldValue(newText, TextRange(newText.length)))

            awaitItem() shouldBe TextFieldValue("", TextRange.Zero)
            awaitItem() shouldBe TextFieldValue(newText, TextRange(newText.length))
        }
    }

    @Test
    fun saveQuery_DelegatesQueryToRepository() = runTest {
        viewModel.saveQuery("mixed nuts")
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.saveQuery("mixed nuts") }
    }

    @Test
    fun updateSearchText_DoesNotSaveQueryToRepository() = runTest {
        viewModel.updateSearchText(TextFieldValue("some text"))
        advanceUntilIdle()

        coVerify(exactly = 0) { repository.saveQuery(any()) }
    }

}
