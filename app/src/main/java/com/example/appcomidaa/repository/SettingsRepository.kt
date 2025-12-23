package com.example.appcomidaa.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val BLUETOOTH = booleanPreferencesKey("bluetooth")
        val VIBRATION = booleanPreferencesKey("vibration")
        val VOLUME = floatPreferencesKey("volume")
    }

    val darkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: false
    }

    val bluetooth: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[BLUETOOTH] ?: false
    }

    val vibration: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[VIBRATION] ?: false
    }

    val volume: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[VOLUME] ?: 50.0f
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = enabled
        }
    }

    suspend fun setBluetooth(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BLUETOOTH] = enabled
        }
    }

    suspend fun setVibration(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[VIBRATION] = enabled
        }
    }

    suspend fun setVolume(value: Float) {
        context.dataStore.edit { preferences ->
            preferences[VOLUME] = value
        }
    }
}
