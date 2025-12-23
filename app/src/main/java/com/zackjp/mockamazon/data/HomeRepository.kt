package com.zackjp.mockamazon.data

import com.zackjp.mockamazon.shared.model.ItemSection
import com.zackjp.mockamazon.shared.model.TopHomeGroup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val homeFakeApiDataSource: HomeFakeApiDataSource,
) {

    suspend fun getTopHomeGroups(): List<TopHomeGroup> = homeFakeApiDataSource.fetchTopHomeGroups()

    suspend fun getHomeSections(): List<ItemSection> = homeFakeApiDataSource.fetchHomeSections()

}
