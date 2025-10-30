package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.ItemSection
import com.example.fakeamazon.shared.model.TopHomeGroup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val homeFakeApiDataSource: HomeFakeApiDataSource,
) {

    suspend fun getTopHomeGroups(): List<TopHomeGroup> = homeFakeApiDataSource.fetchTopHomeGroups()

    suspend fun getHomeSections(): List<ItemSection> = homeFakeApiDataSource.fetchHomeSections()

}
