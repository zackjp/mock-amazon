package com.zackjp.mockamazon.core.data.remote

import androidx.compose.ui.graphics.Color
import com.zackjp.mockamazon.core.model.ProductInfo
import com.zackjp.mockamazon.shared.DispatcherProvider
import com.zackjp.mockamazon.shared.R
import com.zackjp.mockamazon.shared.data.ProductInMemoryDb
import com.zackjp.mockamazon.shared.model.ContextCardResponse
import com.zackjp.mockamazon.shared.model.HeroCarouselCardResponse
import com.zackjp.mockamazon.shared.model.IntentCarouselResponse
import com.zackjp.mockamazon.shared.model.ProductTileResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HomeFakeApiDataSource @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val productInMemoryDb: ProductInMemoryDb,
) {

    suspend fun fetchHeroCarouselCards(): List<HeroCarouselCardResponse> =
        withContext(dispatcherProvider.default) {
            delay(1000) // simulate network delay
            createHeroCarouselCards()
        }

    suspend fun fetchIntentCarousels(): List<IntentCarouselResponse> =
        withContext(dispatcherProvider.default) {
            delay(1000L) // simulate network delay
            createIntentCarousels()
        }

    private fun getProductAsItem(id: Int, showDiscount: Boolean = true): ProductTileResponse =
        productInMemoryDb.getProductById(id)!!.toItem(showDiscount)

    private fun ProductInfo.toItem(showDiscount: Boolean): ProductTileResponse =
        ProductTileResponse(
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

    private fun createIntentCarousels(): List<IntentCarouselResponse> = listOf(
        IntentCarouselResponse(
            "Recommended deals for you",
            listOf(
                ContextCardResponse(
                    "Deals for you",
                    getProductAsItem(R.drawable.item_backpack),
                    getProductAsItem(R.drawable.item_headphones),
                    getProductAsItem(R.drawable.item_detergent),
                    getProductAsItem(R.drawable.item_dishwash_detergent),
                ),
                ContextCardResponse(
                    "Inspired by your recent history",
                    getProductAsItem(R.drawable.item_handsoap),
                    getProductAsItem(R.drawable.item_sandwich_bags),
                    getProductAsItem(R.drawable.item_matcha),
                    getProductAsItem(R.drawable.item_kitchen_sponge),
                )
            )
        ),
        IntentCarouselResponse(
            "Buy again",
            listOf(
                ContextCardResponse(
                    "Reorder soon",
                    getProductAsItem(R.drawable.item_deodorant),
                    getProductAsItem(R.drawable.item_soda),
                    getProductAsItem(R.drawable.item_water_filter),
                    getProductAsItem(R.drawable.item_cleaning_gloves),
                ),
                ContextCardResponse(
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