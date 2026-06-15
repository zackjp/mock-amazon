package com.zackjp.mockamazon

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.metrics.performance.FrameData
import androidx.metrics.performance.JankStats
import com.zackjp.mockamazon.analytics.api.AmazonAnalytics
import com.zackjp.mockamazon.analytics.api.model.AnalyticsEvent
import com.zackjp.mockamazon.app.ui.App
import com.zackjp.mockamazon.ui.theme.MockAmazonTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.time.Duration.Companion.nanoseconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var jankStats: JankStats

    @Inject
    lateinit var analytics: AmazonAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize JankStats for current window, after decor is set (eg, enableEdgeToEdge())
        jankStats = JankStats.createAndTrack(window, ::logJankStats)

        setContent {
            WindowCompat.getInsetsController(
                window,
                LocalView.current
            ).isAppearanceLightStatusBars = true

            MockAmazonTheme {
                App()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        analytics.logEvent(AnalyticsEvent.AppOpen)
    }

    override fun onResume() {
        super.onResume()
        jankStats.isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        jankStats.isTrackingEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        jankStats.isTrackingEnabled = false
    }

    private fun logJankStats(frameData: FrameData) {
        if (frameData.isJank) {
            val frameMs = frameData.frameDurationUiNanos.nanoseconds.inWholeMilliseconds
            var msg = "frameMs = ${frameMs}\n"
            msg += frameData.states.joinToString(separator = "\n") { "${it.key} = ${it.value}" }
            Log.d("JankStats", "Jankiness detected: $msg")
        }
    }

}
