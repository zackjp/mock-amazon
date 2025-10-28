package com.example.fakeamazon.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.shared.DispatcherProvider
import com.example.fakeamazon.shared.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _cartItem = MutableStateFlow<CartItem?>(null)
    val cartItem = _cartItem.asStateFlow()

    fun load() {
        viewModelScope.async(dispatcherProvider.default) {
            _cartItem.value = cartRepository.getCartItem()
        }
    }

}
