package com.zackjp.mockamazon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.zackjp.mockamazon.analytics.api.AmazonAnalytics
import com.zackjp.mockamazon.analytics.api.model.AnalyticsEvent
import com.zackjp.mockamazon.app.ui.App
import com.zackjp.mockamazon.ui.theme.MockAmazonTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var analytics: AmazonAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

}
