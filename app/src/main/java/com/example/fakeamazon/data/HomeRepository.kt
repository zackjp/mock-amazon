package com.example.fakeamazon.data

import androidx.compose.ui.graphics.Color
import com.example.fakeamazon.R
import com.example.fakeamazon.base.DispatcherProvider
import com.example.fakeamazon.model.Item
import com.example.fakeamazon.model.ItemGroup
import com.example.fakeamazon.model.ItemSection
import com.example.fakeamazon.model.TopHomeGroup
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    val dispatcherProvider: DispatcherProvider
) {

    private val mockTopHomeGroups: List<TopHomeGroup> = listOf(
        TopHomeGroup(
            "More top\npicks for you",
            Color(0xFF3A6DB1),
            listOf(
                Item(imageRes = R.drawable.item_headphones),
                Item(imageRes = R.drawable.item_backpack),
                Item(imageRes = R.drawable.item_matcha),
                Item(imageRes = R.drawable.item_handsoap),
                Item(imageRes = R.drawable.item_detergent),
            )
        ),
        TopHomeGroup(
            "Kitchen\ncorner",
            Color(0xFF6AD17D),
            listOf(
                Item(imageRes = R.drawable.item_kitchen_sponge),
                Item(imageRes = R.drawable.item_matcha),
                Item(imageRes = R.drawable.item_sandwich_bags),
                Item(imageRes = R.drawable.item_dishwash_detergent),
                Item(imageRes = R.drawable.item_handsoap),
            )
        ),
        TopHomeGroup(
            "Amazon picks\nfor you",
            Color(0xFFED7571),
            listOf(
                Item(imageRes = R.drawable.item_game_monopoly_deal),
                Item(imageRes = R.drawable.item_game_catan),
                Item(imageRes = R.drawable.item_game_ra),
                Item(imageRes = R.drawable.item_game_lost_cities),
                Item(imageRes = R.drawable.item_game_forest_shuffle),
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
                            Item(R.drawable.item_backpack, 0.17f),
                            Item(R.drawable.item_headphones, 0.2f),
                            Item(R.drawable.item_detergent, 0.12f),
                            Item(R.drawable.item_dishwash_detergent, 0.13f),
                        ),
                        ItemGroup(
                            "Inspired by your recent history",
                            Item(R.drawable.item_handsoap, 0.11f),
                            Item(R.drawable.item_sandwich_bags, 0.07f),
                            Item(R.drawable.item_matcha, 0.10f),
                            Item(R.drawable.item_kitchen_sponge, 0.09f),
                        )
                    )
                ),
                ItemSection(
                    "Buy again",
                    listOf(
                        ItemGroup(
                            "Reorder soon",
                            Item(R.drawable.item_deodorant, 0.17f),
                            Item(R.drawable.item_soda, 0.2f),
                            Item(R.drawable.item_water_filter, 0.12f),
                            Item(R.drawable.item_cleaning_gloves, 0.13f),
                        ),
                        ItemGroup(
                            "Home & Kitchen",
                            Item(R.drawable.item_kitchen_sponge, 0.11f),
                            Item(R.drawable.item_dishwash_detergent, 0.07f),
                            Item(R.drawable.item_handsoap, 0.10f),
                            Item(R.drawable.item_sandwich_bags, 0.09f),
                        )
                    )
                ),
            )
        }

}
