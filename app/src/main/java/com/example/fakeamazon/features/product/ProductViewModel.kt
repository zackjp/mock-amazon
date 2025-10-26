package com.example.fakeamazon.features.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.base.DispatcherProvider
import com.example.fakeamazon.data.ProductDataSource
import com.example.fakeamazon.model.ProductInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val productDataSource: ProductDataSource,
) : ViewModel() {

    private val _productInfo = MutableStateFlow<ProductInfo?>(null)
    val productInfo = _productInfo.asStateFlow()

    fun load(productId: Int) {
        viewModelScope.async(dispatcherProvider.default) {
            delay(500)
            _productInfo.value = productDataSource.getProductById(productId)
        }
    }

}
