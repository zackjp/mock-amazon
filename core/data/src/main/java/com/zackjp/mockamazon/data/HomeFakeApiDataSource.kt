package com.zackjp.mockamazon.data

import androidx.compose.ui.graphics.Color
import com.zackjp.mockamazon.shared.DispatcherProvider
import com.zackjp.mockamazon.shared.R
import com.zackjp.mockamazon.shared.data.ProductInMemoryDb
import com.zackjp.mockamazon.shared.model.CarouselCardResponse
import com.zackjp.mockamazon.shared.model.CarouselItemResponse
import com.zackjp.mockamazon.shared.model.CategoryCarouselResponse
import com.zackjp.mockamazon.shared.model.HeroCarouselCardResponse
import com.zackjp.mockamazon.shared.model.ProductInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeFakeApiDataSource @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val productInMemoryDb: ProductInMemoryDb,
) {

    suspend fun fetchHeroCarouselCards(): List<HeroCarouselCardResponse> =
        withContext(dispatcherProvider.default) {
            delay(1000) // simulate network delay
            createHeroCarouselCards()
        }

    suspend fun fetchCategoryCarousels(): List<CategoryCarouselResponse> =
        withContext(dispatcherProvider.default) {
            delay(1000L) // simulate network delay
            createCategoryCarousels()
        }

    private fun getProductAsItem(id: Int, showDiscount: Boolean = true): CarouselItemResponse =
        productInMemoryDb.getProductById(id)!!.toItem(showDiscount)

    private fun ProductInfo.toItem(showDiscount: Boolean): CarouselItemResponse =
        CarouselItemResponse(
            id = id,
            imageRes = imageId,
            discount = if (showDiscount) discount else null,
        )

    private fun createHeroCarouselCards(): List<HeroCarouselCardResponse> = listOf(
        HeroCarouselCardResponse(
            "More top\npicks for you",
            Color(0xFF3A6DB1),
            listOf(
                getProductAsItem(R.drawable.item_sneaker_allbirds_treeglider),
                getProductAsItem(R.drawable.item_sneaker_adidas_ubounce),
                getProductAsItem(R.drawable.item_sneaker_allbirds_treedasher2),
                getProductAsItem(R.drawable.item_sneaker_underarmour_chargedassert9),
                getProductAsItem(R.drawable.item_sneaker_adidas_swiftrun1),
            )
        ),
        HeroCarouselCardResponse(
            "Snack time\nfor everyone",
            Color(0xFF6AD17D),
            listOf(
                getProductAsItem(R.drawable.item_snack_larabar_pbchocolatechip),
                getProductAsItem(R.drawable.item_snack_wonderful_pistachios),
                getProductAsItem(R.drawable.item_snack_bluediamond_almonds),
                getProductAsItem(R.drawable.item_snack_dots_originalpretzels),
                getProductAsItem(R.drawable.item_snack_popsecret_popcorn),
            )
        ),
        HeroCarouselCardResponse(
            "Amazon picks\nfor you",
            Color(0xFFED7571),
            listOf(
                getProductAsItem(R.drawable.item_game_monopoly_deal),
                getProductAsItem(R.drawable.item_game_catan),
                getProductAsItem(R.drawable.item_game_ra),
                getProductAsItem(R.drawable.item_game_lost_cities),
                getProductAsItem(R.drawable.item_game_forest_shuffle),
            )
        )
    )

    private fun createCategoryCarousels(): List<CategoryCarouselResponse> = listOf(
        CategoryCarouselResponse(
            "Recommended deals for you",
            listOf(
                CarouselCardResponse(
                    "Deals for you",
                    getProductAsItem(R.drawable.item_backpack),
                    getProductAsItem(R.drawable.item_headphones),
                    getProductAsItem(R.drawable.item_detergent),
                    getProductAsItem(R.drawable.item_dishwash_detergent),
                ),
                CarouselCardResponse(
                    "Inspired by your recent history",
                    getProductAsItem(R.drawable.item_handsoap),
                    getProductAsItem(R.drawable.item_sandwich_bags),
                    getProductAsItem(R.drawable.item_matcha),
                    getProductAsItem(R.drawable.item_kitchen_sponge),
                )
            )
        ),
        CategoryCarouselResponse(
            "Buy again",
            listOf(
                CarouselCardResponse(
                    "Reorder soon",
                    getProductAsItem(R.drawable.item_deodorant),
                    getProductAsItem(R.drawable.item_soda),
                    getProductAsItem(R.drawable.item_water_filter),
                    getProductAsItem(R.drawable.item_cleaning_gloves),
                ),
                CarouselCardResponse(
                    "Home & Kitchen",
                    getProductAsItem(R.drawable.item_kitchen_sponge),
                    getProductAsItem(R.drawable.item_dishwash_detergent),
                    getProductAsItem(R.drawable.item_handsoap),
                    getProductAsItem(R.drawable.item_sandwich_bags),
                )
            )
        ),
    )

}