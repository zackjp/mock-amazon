package com.example.fakeamazon.features.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.data.ProductInMemoryDb
import com.example.fakeamazon.shared.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val productInMemoryDb: ProductInMemoryDb,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun load(productId: Int) {
        viewModelScope.launch(dispatcherProvider.default) {
            delay(500)
            val productInfo = productInMemoryDb.getProductById(productId)
            if (productInfo == null) {
                _uiState.update { ProductUiState.Error }
            } else {
                _uiState.update { ProductUiState.Loaded(productInfo) }
            }
        }
    }

    fun addToCart(productId: Int) {
        if ((_uiState.value as? ProductUiState.Loaded)?.addToCartState != AddToCartState.Inactive) {
            return
        }

        _uiState.updateIf<ProductUiState.Loaded> { current ->
            current.copy(addToCartState = AddToCartState.Adding)
        }

        viewModelScope.launch(dispatcherProvider.default) {
            cartRepository.addToCart(productId)

            _uiState.updateIf<ProductUiState.Loaded> { current ->
                current.copy(addToCartState = AddToCartState.Added)
            }
        }
    }

    fun onCartAddedViewed() {
        _uiState.updateIf<ProductUiState.Loaded> { current ->
            current.copy(
                addToCartState = AddToCartState.Inactive
            )
        }
    }

}

private inline fun <reified T> MutableStateFlow<in T>.updateIf(updateBlock: (T) -> T) {
    update { current ->
        if (current is T) updateBlock(current) else current
    }
}
