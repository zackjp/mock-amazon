package com.example.fakeamazon.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.shared.DispatcherProvider
import com.example.fakeamazon.shared.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _cartItem = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItem.asStateFlow()

    fun load() {
        viewModelScope.launch(dispatcherProvider.default) {
            _cartItem.value = cartRepository.getCartItems()
        }
    }

    fun removeByProductId(productId: Int) {
        viewModelScope.launch(dispatcherProvider.default) {
            cartRepository.removeByProductId(productId)
            _cartItem.value = cartRepository.getCartItems()
        }
    }

}
