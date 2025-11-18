package com.example.fakeamazon.features.search

import app.cash.turbine.test
import com.example.fakeamazon.data.SearchApiDataSource
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.fakeInfo
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
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
        coEvery { searchApiDataSource.getSearchResults(searchString) } returns expectedResults
        val viewModel = SearchResultsViewModel(searchApiDataSource)

        viewModel.searchResults.test {
            viewModel.load(searchString)

            awaitItem() shouldBe emptyList()
            awaitItem() shouldBe expectedResults
        }
    }

}
