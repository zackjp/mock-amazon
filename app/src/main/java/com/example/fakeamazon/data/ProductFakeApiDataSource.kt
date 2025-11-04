package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.ProductInfo
import kotlinx.coroutines.delay
import javax.inject.Inject

class ProductFakeApiDataSource @Inject constructor(
    private val productInMemoryDb: ProductInMemoryDb
) {

    suspend fun getProductById(productId: Int): ProductInfo? {
        delay(500)
        return productInMemoryDb.getProductById(productId)
    }

    suspend fun getSimilarProducts(productId: Int): List<ProductInfo> {
        delay(100)
        return productInMemoryDb.getSimilarProducts(productId)
    }

}
