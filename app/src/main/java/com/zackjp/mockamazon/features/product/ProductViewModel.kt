package com.zackjp.mockamazon.features.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zackjp.mockamazon.data.CartRepository
import com.zackjp.mockamazon.data.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun load(productId: Int) {
        viewModelScope.launch {
            val productInfo = productRepository.getProductById(productId)

            if (productInfo == null) {
                _uiState.update { ProductUiState.Error }
            } else {
                val similarProducts = productRepository.getSimilarProducts(productId)
                _uiState.update {
                    ProductUiState.Loaded(
                        productInfo = productInfo,
                        similarProducts = similarProducts,
                    )
                }
            }
        }
    }

    fun addToCart() {
        val currentState = _uiState.value
        if (currentState !is ProductUiState.Loaded) {
            return
        }

        if (currentState.addToCartState != AddToCartState.Inactive) {
            return
        }

        _uiState.updateIf<ProductUiState.Loaded> { current ->
            current.copy(addToCartState = AddToCartState.Adding)
        }

        viewModelScope.launch {
            cartRepository.addToCart(currentState.productInfo.id)

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
