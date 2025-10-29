package com.example.fakeamazon.features.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.data.ProductInMemoryDb
import com.example.fakeamazon.shared.DispatcherProvider
import com.example.fakeamazon.shared.model.ProductInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val productInMemoryDb: ProductInMemoryDb,
) : ViewModel() {

    private val _productInfo = MutableStateFlow<ProductInfo?>(null)
    val productInfo = _productInfo.asStateFlow()

    fun load(productId: Int) {
        viewModelScope.async(dispatcherProvider.default) {
            delay(500)
            _productInfo.value = productInMemoryDb.getProductById(productId)
        }
    }

    fun addToCart(productId: Int) {
        viewModelScope.async(dispatcherProvider.default) {
            cartRepository.addToCart(productId)
        }
    }

}
