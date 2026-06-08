package com.zackjp.mockamazon.analytics.api.model

sealed interface AnalyticsEvent {

    object AppOpen: AnalyticsEvent

}
