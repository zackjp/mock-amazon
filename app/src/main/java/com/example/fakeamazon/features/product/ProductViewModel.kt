package com.example.fakeamazon.features.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.R
import com.example.fakeamazon.base.DispatcherProvider
import com.example.fakeamazon.model.ProductInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _productInfo = MutableStateFlow<ProductInfo?>(null)
    val productInfo = _productInfo.asStateFlow()

    fun load() {
        viewModelScope.async(dispatcherProvider.default) {
            delay(500)
            _productInfo.value = ProductInfo(
                storeName = "Thames & Kosmos",
                storeInitials = "TK",
                title = "Lost Cities Card Game - with 6th Expedition - Thames & Kosmos Store - Designed By Reiner Knizia",
                productRating = 4.4f,
                imageId = R.drawable.item_game_lost_cities,
            )
        }
    }

}