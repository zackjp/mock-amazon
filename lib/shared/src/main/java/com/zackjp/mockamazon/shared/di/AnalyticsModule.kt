package com.zackjp.mockamazon.shared.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    fun provideFirebaseAnalytics(@ApplicationContext appContext: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(appContext)
    }

}
