package com.kreggscode.enstienquotes.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    
    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val DAILY_NOTIFICATION_KEY = booleanPreferencesKey("daily_notification")
    }
    
    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false
    }
    
    val isDailyNotificationEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DAILY_NOTIFICATION_KEY] ?: true
    }
    
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }
    
    suspend fun setDailyNotification(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DAILY_NOTIFICATION_KEY] = enabled
        }
    }
}
