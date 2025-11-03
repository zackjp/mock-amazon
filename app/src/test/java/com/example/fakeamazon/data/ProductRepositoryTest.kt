package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.fakeInfo
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ProductRepositoryTest {

    @Test
    fun getProductById_ReturnsProduct() = runTest {
        val expectedProduct = ProductInfo.fakeInfo(123)
        val productFakeApiDataSource = mockk<ProductFakeApiDataSource>()
        coEvery { productFakeApiDataSource.getProductById(123) } returns expectedProduct
        val repository = ProductRepository(productFakeApiDataSource)

        repository.getProductById(123) shouldBe expectedProduct
    }

}
