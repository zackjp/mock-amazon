package com.zackjp.mockamazon.analytics.impl.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.zackjp.mockamazon.analytics.api.AmazonAnalytics
import com.zackjp.mockamazon.analytics.impl.FirebaseAnalyticsImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {

    @Singleton
    @Binds
    abstract fun provideAmazonAnalytics(firebaseAnalyticsImpl: FirebaseAnalyticsImpl): AmazonAnalytics

    companion object {
        @Singleton
        @Provides
        fun provideFirebaseAnalytics(@ApplicationContext appContext: Context): FirebaseAnalytics {
            return FirebaseAnalytics.getInstance(appContext)
        }
    }

}
