package com.zackjp.mockamazon.feature.search.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.zackjp.mockamazon.feature.search.LocalSearchHistoryRepository
import com.zackjp.mockamazon.feature.search.SearchHistoryRepository
import com.zackjp.mockamazon.shared.AppScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SearchHistoryModule {

    @Binds
    abstract fun bindSearchHistoryRepository(impl: LocalSearchHistoryRepository): SearchHistoryRepository

    companion object {
        @Provides
        @Singleton
        fun provideSearchHistoryDataStore(
            @ApplicationContext context: Context,
            @AppScope appScope: CoroutineScope,
        ): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
                scope = appScope,
                produceFile = { context.preferencesDataStoreFile("search_history") },
            )
        }
    }
}
