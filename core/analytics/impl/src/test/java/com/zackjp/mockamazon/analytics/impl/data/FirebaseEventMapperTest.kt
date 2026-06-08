package com.zackjp.mockamazon.analytics.impl.data

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import com.zackjp.mockamazon.analytics.api.model.AnalyticsEvent
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirebaseEventMapperTest {

    private val paramsBuilder = mockk<ParametersBuilder>(relaxed = true)

    private lateinit var eventMapper: FirebaseEventMapper

    @BeforeEach
    fun setUp() {
        eventMapper = FirebaseEventMapper()
    }

    @Test
    fun map_AppOpen() {
        val actual = AnalyticsEvent.AppOpen

        val expected = eventMapper.map(actual)

        expected.name shouldBe FirebaseAnalytics.Event.APP_OPEN
        expected.paramBuilder(paramsBuilder)
        verify { paramsBuilder wasNot Called }
    }

}
