package com.zackjp.mockamazon.data

import com.zackjp.mockamazon.shared.DispatcherProvider
import com.zackjp.mockamazon.shared.model.Cart
import com.zackjp.mockamazon.shared.model.toCartItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartFakeApiDataSource @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val productInMemoryDb: ProductInMemoryDb,
) {

    private val cartProductIdQuantityMap = Collections.synchronizedMap(LinkedHashMap<Int, Int>())

    suspend fun addToCart(productId: Int): Boolean = withContext(dispatcherProvider.default) {
        delay(500)

        val productInfo = productInMemoryDb.getProductById(productId)
        if (productInfo == null) {
            return@withContext false
        }

        cartProductIdQuantityMap.compute(productId) { _, value -> (value ?: 0) + 1 }
        return@withContext true
    }

    suspend fun setQuantity(productId: Int, quantity: Int) {
        delay(500)

        if (quantity > 0) {
            cartProductIdQuantityMap.put(productId, quantity)
        } else {
            cartProductIdQuantityMap.remove(productId)
        }
    }

    suspend fun removeByProductId(productId: Int): Unit = withContext(dispatcherProvider.default) {
        delay(1250)
        cartProductIdQuantityMap.remove(productId)
    }

    suspend fun getCart(): Cart = withContext(dispatcherProvider.default) {
        delay(300)

        val cartItems = cartProductIdQuantityMap
            .map { productInMemoryDb.getProductById(it.key) to it.value }
            .filter { (product, quantity) -> product != null }
            .map { (product, quantity) -> product!!.toCartItem(quantity) }
            .reversed()

        Cart(
            cartItems = cartItems,
            totalPriceUSD = cartItems.sumOf {
                it.priceUSD.toDouble() * it.quantity
            }.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toFloat(),
        )
    }

    suspend fun decrementByProductId(productId: Int): Unit = withContext(dispatcherProvider.default) {
        cartProductIdQuantityMap.compute(productId) { key, value ->
            val updatedQuantity = (value ?: 0) - 1
            if (updatedQuantity <= 0) null else updatedQuantity
        }
    }

}
