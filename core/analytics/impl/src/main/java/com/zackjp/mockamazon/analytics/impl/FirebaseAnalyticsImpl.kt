package com.zackjp.mockamazon.analytics.impl

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.zackjp.mockamazon.analytics.api.AmazonAnalytics
import com.zackjp.mockamazon.analytics.api.model.AnalyticsEvent
import com.zackjp.mockamazon.analytics.impl.data.FirebaseEvent
import com.zackjp.mockamazon.analytics.impl.data.FirebaseEventMapper
import javax.inject.Inject

internal class FirebaseAnalyticsImpl @Inject constructor(
    private val delegate: FirebaseAnalytics,
    private val eventMapper: FirebaseEventMapper,
) : AmazonAnalytics {

    override fun logEvent(event: AnalyticsEvent) {
        val firebaseEvent: FirebaseEvent = eventMapper.map(event)
        delegate.logEvent(firebaseEvent.name, firebaseEvent.paramBuilder)
    }

}
