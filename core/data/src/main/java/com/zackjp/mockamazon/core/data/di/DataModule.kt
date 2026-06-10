package com.zackjp.mockamazon.core.data.di

import com.zackjp.mockamazon.core.data.HomeRepository
import com.zackjp.mockamazon.core.data.HomeRepositoryImpl
import com.zackjp.mockamazon.core.data.ProductRepository
import com.zackjp.mockamazon.core.data.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

}
