package com.example.fakeamazon.features.search

import com.example.fakeamazon.data.SearchApiDataSource
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.fakeInfo
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SearchResultsViewModelTest {

    @Test
    fun load_LoadsSearchResults() = runTest {
        val searchString = "test search string"
        val searchApiDataSource = mockk<SearchApiDataSource>()
        val expectedResults = listOf(
            ProductInfo.fakeInfo(123),
            ProductInfo.fakeInfo(456),
        )
        coEvery { searchApiDataSource.getHardcodedSearchResults(searchString) } returns expectedResults
        val viewModel = SearchResultsViewModel(searchApiDataSource)

        viewModel.load(searchString)

        viewModel.searchResults.first() shouldBe emptyList()
        testScheduler.advanceUntilIdle()
        viewModel.searchResults.first() shouldBe expectedResults
    }

}
