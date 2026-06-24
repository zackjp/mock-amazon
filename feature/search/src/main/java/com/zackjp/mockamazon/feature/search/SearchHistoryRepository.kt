package com.zackjp.mockamazon.feature.search

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun observeHistory(): Flow<List<String>>
    suspend fun saveQuery(query: String)
    suspend fun removeQuery(query: String)
}
