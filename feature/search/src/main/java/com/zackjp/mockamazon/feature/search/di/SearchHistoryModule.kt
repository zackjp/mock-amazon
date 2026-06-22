package com.zackjp.mockamazon.feature.search.di

import com.zackjp.mockamazon.feature.search.FakeSearchHistoryRepository
import com.zackjp.mockamazon.feature.search.SearchHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SearchHistoryModule {

    @Binds
    abstract fun bindSearchHistoryRepository(impl: FakeSearchHistoryRepository): SearchHistoryRepository
}
