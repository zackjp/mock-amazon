package com.zackjp.mockamazon.analytics.impl.data

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import com.zackjp.mockamazon.analytics.api.model.AnalyticsEvent
import javax.inject.Inject


internal data class FirebaseEvent(
    val name: String,
    val paramBuilder: ParametersBuilder.() -> Unit,
)


internal class FirebaseEventMapper @Inject internal constructor() {

    fun map(event: AnalyticsEvent): FirebaseEvent {
        return when (event) {
            is AnalyticsEvent.AppOpen -> FirebaseEvent(FirebaseAnalytics.Event.APP_OPEN) {}
        }
    }

}
