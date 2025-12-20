package com.example.mockamazon.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mockamazon.shared.runIf
import com.example.mockamazon.shared.updateIf
import com.example.mockamazon.data.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow<CartScreenState>(CartScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _screenState.updateIf<CartScreenState.Loaded> { current ->
                current.copy(isReloading = true)
            }
            reloadCartItems()
        }
    }

    fun removeByProductId(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeByProductId(productId)
            reloadCartItems()
        }
    }

    fun incrementCartItem(productId: Int) {
        viewModelScope.launch {
            setRepositoryQuantity(productId, 1)
            reloadCartItems()
        }
    }

    fun decrementCartItem(productId: Int) {
        viewModelScope.launch {
            setRepositoryQuantity(productId, -1)
            reloadCartItems()
        }
    }

    private suspend fun setRepositoryQuantity(productId: Int, quantityChange: Int) {
        _screenState.value.runIf<CartScreenState.Loaded> {
            val currentCartItem = cartItems.find { it.id == productId }
            currentCartItem?.let {
                val currentQuantity = it.quantity
                cartRepository.setQuantity(productId, currentQuantity + quantityChange)
            }
        }
    }

    private suspend fun reloadCartItems() {
        val updatedCart = cartRepository.getCart()
        _screenState.value = CartScreenState.Loaded(
            cartItems = updatedCart.cartItems,
            totalPriceUSD = updatedCart.totalPriceUSD,
        )
    }

}
