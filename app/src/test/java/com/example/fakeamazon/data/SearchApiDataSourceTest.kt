package com.example.fakeamazon.data

import com.example.fakeamazon.R
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.fakeInfo
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchApiDataSourceTest {

    private val productInMemoryDb = mockk<ProductInMemoryDb>()
    private val hardcodedResults = listOf(
        ProductInfo.fakeInfo(123),
        ProductInfo.fakeInfo(456),
    )

    private lateinit var dataSource: SearchApiDataSource

    @BeforeEach
    fun setUp() {
        every {
            productInMemoryDb.getSimilarProducts(R.drawable.item_snack_amazon_pbpretzels)
        } returns hardcodedResults
        dataSource = SearchApiDataSource(productInMemoryDb)
    }

    @Test
    fun getHardcodedSearchResults_ReturnsResults() = runTest {
        val actual = dataSource.getHardcodedSearchResults("ignored")

        actual shouldBe hardcodedResults
    }

}
