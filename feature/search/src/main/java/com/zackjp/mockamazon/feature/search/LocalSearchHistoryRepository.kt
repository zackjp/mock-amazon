package com.zackjp.mockamazon.feature.search

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalSearchHistoryRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SearchHistoryRepository {

    private val historyKey = stringPreferencesKey("search_history")

    override fun observeHistory(): Flow<List<String>> = dataStore.data
        // TODO (Zack): Log IOException once non-Android-specific logging strategy is added
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { prefs -> prefs[historyKey].decodeOrEmpty() }

    override suspend fun saveQuery(query: String) {
        dataStore.edit { prefs ->
            val current = prefs[historyKey].decodeOrEmpty()
            val updated = buildList {
                add(query)
                addAll(current.filterNot { it == query })
                if (size > MAX_HISTORY_SIZE) removeAt(MAX_HISTORY_SIZE)
            }
            prefs[historyKey] = Json.encodeToString(updated)
        }
    }

    override suspend fun removeQuery(query: String) {
        dataStore.edit { prefs ->
            val history = prefs[historyKey].decodeOrEmpty()
            val updatedHistory = history.filterNot { q -> q == query }
            prefs[historyKey] = Json.encodeToString(updatedHistory)
        }
    }

    // TODO (Zack): Log SerializationException once non-Android-specific logging strategy is added
    private fun String?.decodeOrEmpty(): List<String> =
        this?.let { runCatching { Json.decodeFromString<List<String>>(it) }.getOrDefault(emptyList()) }
            ?: emptyList()

    companion object {
        private const val MAX_HISTORY_SIZE = 10
    }
}
