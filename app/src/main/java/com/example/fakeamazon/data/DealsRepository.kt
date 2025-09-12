package com.example.fakeamazon.data

import com.example.fakeamazon.R
import com.example.fakeamazon.model.Recommendation
import com.example.fakeamazon.model.RecommendationGroup

object DealsRepository {

    val RECOMMENDED_DEALS: List<RecommendationGroup> = listOf(
        RecommendationGroup(
            "Deals for you",
            Recommendation(R.drawable.item_backpack, 0.17f),
            Recommendation(R.drawable.item_headphones, 0.2f),
            Recommendation(R.drawable.item_detergent, 0.12f),
            Recommendation(R.drawable.item_dishwash_detergent, 0.13f),
        ),
        RecommendationGroup(
            "Inspired by your recent history",
            Recommendation(R.drawable.item_handsoap, 0.11f),
            Recommendation(R.drawable.item_sandwich_bags, 0.07f),
            Recommendation(R.drawable.item_matcha, 0.10f),
            Recommendation(R.drawable.item_kitchen_sponge, 0.09f),
        )
    )

}
