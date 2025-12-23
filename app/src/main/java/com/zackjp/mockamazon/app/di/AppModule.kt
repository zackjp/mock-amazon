package com.zackjp.mockamazon.app.di

import com.zackjp.mockamazon.shared.DefaultDispatcherProvider
import com.zackjp.mockamazon.shared.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindDispatcherProvider(provider: DefaultDispatcherProvider): DispatcherProvider

}