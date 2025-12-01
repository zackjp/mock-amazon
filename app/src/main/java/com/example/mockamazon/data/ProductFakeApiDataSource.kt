package com.example.mockamazon.data

import com.example.mockamazon.shared.DispatcherProvider
import com.example.mockamazon.shared.model.ProductInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductFakeApiDataSource @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val productInMemoryDb: ProductInMemoryDb,
) {

    suspend fun getProductById(productId: Int): ProductInfo? =
        withContext(dispatcherProvider.default) {
            delay(500)
            productInMemoryDb.getProductById(productId)
        }

    suspend fun getSimilarProducts(productId: Int): List<ProductInfo> =
        withContext(dispatcherProvider.default) {
            delay(100)
            productInMemoryDb.getSimilarProducts(productId)
        }

}
