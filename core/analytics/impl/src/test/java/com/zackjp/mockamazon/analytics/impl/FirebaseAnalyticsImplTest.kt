package com.zackjp.mockamazon.analytics.impl

import com.google.firebase.analytics.FirebaseAnalytics
import com.zackjp.mockamazon.analytics.api.model.AnalyticsEvent
import com.zackjp.mockamazon.analytics.impl.data.FirebaseEvent
import com.zackjp.mockamazon.analytics.impl.data.FirebaseEventMapper
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test


class FirebaseAnalyticsImplTest {

    @Test
    fun logEvent_LogsTheMappedEvent() {
        val delegate = mockk<FirebaseAnalytics>()
        val eventMapper = mockk<FirebaseEventMapper>()
        val actualEvent = mockk<AnalyticsEvent>()
        val expectedEvent = FirebaseEvent("expected_event") {}
        every { eventMapper.map(actualEvent) } returns expectedEvent
        every { delegate.logEvent(any(), any()) } just runs

        val analyticsImpl = FirebaseAnalyticsImpl(
            delegate = delegate,
            eventMapper = eventMapper,
        )

        analyticsImpl.logEvent(actualEvent)

        // logEvent(String, ParametersBuilder.()) cannot be mocked because it is inline.
        // It calls logEvent(String, Bundle) under the hood.
        verify { delegate.logEvent(expectedEvent.name, any()) }
    }

}
