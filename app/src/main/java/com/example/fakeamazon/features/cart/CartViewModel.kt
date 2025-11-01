package com.example.fakeamazon.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.shared.DispatcherProvider
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

    private val _screenState = MutableStateFlow<CartScreenState>(CartScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    fun load() {
        viewModelScope.launch(dispatcherProvider.default) {
            val cartItems = cartRepository.getCartItems()
            _screenState.value = CartScreenState.Loaded(cartItems)
        }
    }

    fun removeByProductId(productId: Int) {
        viewModelScope.launch(dispatcherProvider.default) {
            cartRepository.removeByProductId(productId)

            val updatedCartItems = cartRepository.getCartItems()
            _screenState.value = CartScreenState.Loaded(updatedCartItems)
        }
    }

}
