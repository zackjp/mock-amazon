package com.example.fakeamazon.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.CartRepository
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
            cartRepository.addToCart(productId)
            reloadCartItems()
        }
    }

    private suspend fun reloadCartItems() {
        val updatedCartItems = cartRepository.getCartItems()
        _screenState.value = CartScreenState.Loaded(updatedCartItems)
    }

}
