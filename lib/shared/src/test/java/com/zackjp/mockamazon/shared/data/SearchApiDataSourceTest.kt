package com.zackjp.mockamazon.shared.data

import com.zackjp.mockamazon.shared.model.ProductInfo
import com.zackjp.mockamazon.shared.testutils.model.fakeInfo
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchApiDataSourceTest {

    private companion object {
        private const val VALID_SEARCH_STRING = "test search string"
    }

    private val productInMemoryDb = mockk<ProductInMemoryDb>()
    private val expectedResults = listOf(
        ProductInfo.fakeInfo(123),
        ProductInfo.fakeInfo(456),
    )

    private lateinit var dataSource: SearchApiDataSource

    @BeforeEach
    fun setUp() {
        every {
            productInMemoryDb.findProducts(VALID_SEARCH_STRING)
        } returns expectedResults
        dataSource = SearchApiDataSource(productInMemoryDb)
    }

    @Test
    fun getSearchResults_ReturnsResults() = runTest {
        val actual = dataSource.getSearchResults(VALID_SEARCH_STRING)

        actual shouldBe expectedResults
    }

}
