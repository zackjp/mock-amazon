package com.zackjp.mockamazon.core.data

import com.zackjp.mockamazon.model.ProductInfo

interface ProductRepository {

    suspend fun getProductById(productId: Int): ProductInfo?

    suspend fun getSimilarProducts(productId: Int): List<ProductInfo>

}
