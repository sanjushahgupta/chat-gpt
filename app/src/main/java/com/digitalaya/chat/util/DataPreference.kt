package com.digitalaya.chat.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_preferences")

class UserPreference(val context: Context) {
    companion object {
        val Index = stringPreferencesKey(" ")
    }

    val apiKeyStore = context.dataStore.data.map { preferences ->
        preferences[Index] ?: " "
    }

    suspend fun savePlatformIndexStatus(status: String) {
        context.dataStore.edit { preferences ->
            preferences[Index] = status
        }
    }
}