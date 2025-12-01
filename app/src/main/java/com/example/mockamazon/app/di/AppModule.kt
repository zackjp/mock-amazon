package com.example.mockamazon.app.di

import com.example.mockamazon.shared.DefaultDispatcherProvider
import com.example.mockamazon.shared.DispatcherProvider
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