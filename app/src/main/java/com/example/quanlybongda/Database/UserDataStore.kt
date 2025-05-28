package com.example.quanlybongda.Database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val USER_PREFERENCES_NAME = "user_preferences";
private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class UserDataStore(context : Context) {
    private val SESSION_TOKEN = stringPreferencesKey("session_token");

    val loggedInFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SESSION_TOKEN] ?: ""
        }

    suspend fun saveSessionToken(sessionToken: String, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[SESSION_TOKEN] = sessionToken;
        }
    }
}