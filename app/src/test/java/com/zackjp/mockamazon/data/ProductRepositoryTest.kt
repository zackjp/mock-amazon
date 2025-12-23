package com.zackjp.mockamazon.data

import com.zackjp.mockamazon.shared.model.ProductInfo
import com.zackjp.mockamazon.shared.testutils.model.fakeInfo
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductRepositoryTest {

    private companion object {
        private val VALID_PRODUCT_ID = 123
    }

    private val productFakeApiDataSource = mockk<ProductFakeApiDataSource>()

    private lateinit var repository: ProductRepository

    @BeforeEach
    fun setUp() {
        repository = ProductRepository(productFakeApiDataSource)
    }

    @Test
    fun getProductById_ReturnsProduct() = runTest {
        val expectedProduct = ProductInfo.fakeInfo(VALID_PRODUCT_ID)
        coEvery { productFakeApiDataSource.getProductById(VALID_PRODUCT_ID) } returns expectedProduct

        val actual = repository.getProductById(VALID_PRODUCT_ID)

        actual shouldBe expectedProduct
    }

    @Test
    fun getSimilarProducts_ReturnsSimilar() = runTest {
        val expectedSimilarProducts = listOf(
            ProductInfo.fakeInfo(100),
            ProductInfo.fakeInfo(200),
        )
        coEvery { productFakeApiDataSource.getSimilarProducts(VALID_PRODUCT_ID) } returns expectedSimilarProducts

        val actual = repository.getSimilarProducts(VALID_PRODUCT_ID)

        actual shouldBe expectedSimilarProducts
    }

}
