package com.zackjp.mockamazon.shared.data

import com.zackjp.mockamazon.shared.model.ProductInfo
import com.zackjp.mockamazon.shared.testutils.TestDispatcherProvider
import com.zackjp.mockamazon.shared.testutils.model.fakeInfo
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductFakeApiDataSourceTest {

    private companion object {
        private const val VALID_PRODUCT_ID = 123
    }

    private val testDispatcherProvider = TestDispatcherProvider()
    private val testDispatcher = testDispatcherProvider.default

    private val expectedProductInfo = ProductInfo.fakeInfo(VALID_PRODUCT_ID)
    private val expectedSimilarProducts = listOf(
        ProductInfo.fakeInfo(100),
        ProductInfo.fakeInfo(200),
    )

    private lateinit var dataSource: ProductFakeApiDataSource

    @BeforeEach
    fun setUp() {
        val productInMemoryDb = mockk<ProductInMemoryDb>()
        every { productInMemoryDb.getProductById(VALID_PRODUCT_ID) } returns expectedProductInfo
        every { productInMemoryDb.getSimilarProducts(VALID_PRODUCT_ID) } returns expectedSimilarProducts

        dataSource = ProductFakeApiDataSource(
            dispatcherProvider = testDispatcherProvider,
            productInMemoryDb = productInMemoryDb,
        )
    }

    @Test
    fun getProductById_ReturnsProductInfo() = runTest(testDispatcher) {
        val actual = dataSource.getProductById(VALID_PRODUCT_ID)

        actual shouldBe expectedProductInfo
    }

    @Test
    fun getSimilarProducts_ReturnsSimilarProducts() = runTest(testDispatcher) {
        val actual = dataSource.getSimilarProducts(VALID_PRODUCT_ID)

        actual shouldBe expectedSimilarProducts
    }

}
