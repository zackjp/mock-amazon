package com.zackjp.mockamazon.analytics.api

import com.zackjp.mockamazon.analytics.api.model.AnalyticsEvent

interface AmazonAnalytics {

    fun logEvent(event: AnalyticsEvent)

}
