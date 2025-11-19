package com.example.fakeamazon.features.search

import app.cash.turbine.test
import com.example.fakeamazon.data.SearchApiDataSource
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.fakeInfo
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchResultsViewModelTest {

    private companion object {
        private const val VALID_SEARCH_STRING = "valid search string"
        private const val THROWING_SEARCH_STRING = "throwing search string"
        private val expectedSearchResults = listOf(
            ProductInfo.fakeInfo(123),
            ProductInfo.fakeInfo(456),
        )
    }

    private val searchApiDataSource = mockk<SearchApiDataSource>()

    private lateinit var viewModel: SearchResultsViewModel

    @BeforeEach
    fun setUp() {
        coEvery { searchApiDataSource.getSearchResults(VALID_SEARCH_STRING) } returns expectedSearchResults
        coEvery { searchApiDataSource.getSearchResults(THROWING_SEARCH_STRING) } throws Exception("search error exception")

        viewModel = SearchResultsViewModel(searchApiDataSource)
    }

    @Test
    fun init_StartsAsLoading() = runTest {
        viewModel.screenState.test {
            awaitItem() shouldBe SearchResultsScreenState.Loading
        }
    }

    @Test
    fun load_WithValidSearch_LoadsSearchResults() = runTest {
        viewModel.screenState.test {
            viewModel.load(VALID_SEARCH_STRING)

            awaitItem() shouldBe SearchResultsScreenState.Loading
            awaitItem() shouldBe SearchResultsScreenState.Loaded(expectedSearchResults)
        }
    }

    @Test
    fun load_WithSearchException_EmitsError() = runTest {
        viewModel.screenState.test {
            viewModel.load(THROWING_SEARCH_STRING)

            awaitItem() shouldBe SearchResultsScreenState.Loading
            awaitItem() shouldBe SearchResultsScreenState.Error
        }
    }

}
