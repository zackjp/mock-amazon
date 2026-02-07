package com.zackjp.mockamazon.feature.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zackjp.mockamazon.shared.data.CartRepository
import com.zackjp.mockamazon.shared.data.ProductRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ProductViewModel.Factory::class)
class ProductViewModel @AssistedInject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    @Assisted private val productId: Int,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: Int): ProductViewModel
    }

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState = _uiState
        .onStart { load() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProductUiState.Loading,
        )

    private fun load() {
        viewModelScope.launch {
            val productInfo = productRepository.getProductById(productId)

            if (productInfo == null) {
                _uiState.value = ProductUiState.Error
            } else {
                val similarProducts = productRepository.getSimilarProducts(productId)
                _uiState.value = ProductUiState.Loaded(
                    productInfo = productInfo,
                    similarProducts = similarProducts,
                )
            }
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            var productIdToAdd: Int? = null
            _uiState.updateIf<ProductUiState.Loaded> { current ->
                if (current.addToCartState == AddToCartState.Inactive) {
                    productIdToAdd = current.productInfo.id
                    current.copy(addToCartState = AddToCartState.Adding)
                } else {
                    productIdToAdd = null
                    current
                }
            }

            productIdToAdd?.let {
                cartRepository.addToCart(it)

                _uiState.updateIf<ProductUiState.Loaded> { current ->
                    current.copy(addToCartState = AddToCartState.Added)
                }
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
