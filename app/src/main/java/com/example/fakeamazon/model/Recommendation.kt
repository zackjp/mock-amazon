package com.example.fakeamazon.model

import androidx.annotation.DrawableRes

data class RecommendationGroup(
    val rec1: Recommendation,
    val rec2: Recommendation,
    val rec3: Recommendation,
    val rec4: Recommendation,
)

data class Recommendation(
    @DrawableRes val imageRes: Int,
    val discount: Float,
)

fun RecommendationGroup.toList(): List<Recommendation> = listOf(rec1, rec2, rec3, rec4)
