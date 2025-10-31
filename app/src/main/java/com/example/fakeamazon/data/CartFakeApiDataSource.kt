package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.model.toCartItem
import kotlinx.coroutines.delay
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartFakeApiDataSource @Inject constructor(
    private val productInMemoryDb: ProductInMemoryDb,
) {

    private val cartProductIdQuantityMap = Collections.synchronizedMap(LinkedHashMap<Int, Int>())

    suspend fun addToCart(productId: Int): Boolean {
        delay(500)

        val productInfo = productInMemoryDb.getProductById(productId)
        if (productInfo == null) {
            return false
        }

        cartProductIdQuantityMap.compute(productId) { _, value -> (value ?: 0) + 1 }
        return true
    }

    suspend fun getCartItems(): List<CartItem> {
        delay(300)

        return cartProductIdQuantityMap
            .map { productInMemoryDb.getProductById(it.key) to it.value }
            .filter { (product, quantity) -> product != null && quantity > 0 }
            .map { (product, quantity) -> product!!.toCartItem(quantity) }
    }

}
