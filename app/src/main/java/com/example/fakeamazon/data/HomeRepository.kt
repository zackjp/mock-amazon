package com.example.fakeamazon.data

import androidx.compose.ui.graphics.Color
import com.example.fakeamazon.R
import com.example.fakeamazon.base.DispatcherProvider
import com.example.fakeamazon.model.Item
import com.example.fakeamazon.model.ItemGroup
import com.example.fakeamazon.model.ItemSection
import com.example.fakeamazon.model.ProductInfo
import com.example.fakeamazon.model.TopHomeGroup
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val productStaticDataSource: ProductStaticDataSource,
) {

    private val mockTopHomeGroups: List<TopHomeGroup> = listOf(
        TopHomeGroup(
            "More top\npicks for you",
            Color(0xFF3A6DB1),
            listOf(
                getProductAsItem(R.drawable.item_headphones),
                getProductAsItem(R.drawable.item_backpack),
                getProductAsItem(R.drawable.item_matcha),
                getProductAsItem(R.drawable.item_handsoap),
                getProductAsItem(R.drawable.item_detergent),
            )
        ),
        TopHomeGroup(
            "Kitchen\ncorner",
            Color(0xFF6AD17D),
            listOf(
                getProductAsItem(R.drawable.item_kitchen_sponge),
                getProductAsItem(R.drawable.item_matcha),
                getProductAsItem(R.drawable.item_sandwich_bags),
                getProductAsItem(R.drawable.item_dishwash_detergent),
                getProductAsItem(R.drawable.item_handsoap),
            )
        ),
        TopHomeGroup(
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

    suspend fun loadTopHome(): List<TopHomeGroup> =
        withContext(dispatcherProvider.default) {
            delay(300)

            mockTopHomeGroups
        }

    suspend fun loadSections(): List<ItemSection> =
        withContext(dispatcherProvider.default) {
            delay(1500L)

            listOf(
                ItemSection(
                    "Recommended deals for you",
                    listOf(
                        ItemGroup(
                            "Deals for you",
                            getProductAsItem(R.drawable.item_backpack, 0.17f),
                            getProductAsItem(R.drawable.item_headphones, 0.2f),
                            getProductAsItem(R.drawable.item_detergent, 0.12f),
                            getProductAsItem(R.drawable.item_dishwash_detergent, 0.13f),
                        ),
                        ItemGroup(
                            "Inspired by your recent history",
                            getProductAsItem(R.drawable.item_handsoap, 0.11f),
                            getProductAsItem(R.drawable.item_sandwich_bags, 0.07f),
                            getProductAsItem(R.drawable.item_matcha, 0.10f),
                            getProductAsItem(R.drawable.item_kitchen_sponge, 0.09f),
                        )
                    )
                ),
                ItemSection(
                    "Buy again",
                    listOf(
                        ItemGroup(
                            "Reorder soon",
                            getProductAsItem(R.drawable.item_deodorant),
                            getProductAsItem(R.drawable.item_soda),
                            getProductAsItem(R.drawable.item_water_filter),
                            getProductAsItem(R.drawable.item_cleaning_gloves),
                        ),
                        ItemGroup(
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

    private fun getProductAsItem(id: Int, discount: Float? = null): Item =
        productStaticDataSource.getProductById(id)!!.toItem(discount)

    private fun ProductInfo.toItem(discount: Float?): Item =
        Item(
            id = id,
            imageRes = imageId,
            discount = discount,
        )
}
