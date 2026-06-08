package com.zackjp.mockamazon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.zackjp.mockamazon.app.ui.App
import com.zackjp.mockamazon.ui.theme.MockAmazonTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var analytics: FirebaseAnalytics

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

        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
    }

}
