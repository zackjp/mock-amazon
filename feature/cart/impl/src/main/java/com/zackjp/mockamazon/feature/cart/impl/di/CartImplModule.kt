package com.zackjp.mockamazon.feature.cart.impl.di

import com.zackjp.mockamazon.feature.cart.api.data.CartRepository
import com.zackjp.mockamazon.feature.cart.impl.data.CartRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CartImplModule {

    @Binds
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

}
