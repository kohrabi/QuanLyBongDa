package com.example.quanlybongda.Database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.quanlybongda.Database.Schema.User.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val USER_PREFERENCES_NAME = "user_preferences";
private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class UserDataStore(context : Context) {
    private val IS_LOGGED_IN = booleanPreferencesKey("logged_in");

    val loggedInFlow: Flow<Boolean> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    suspend fun saveLoggedIn(loggedIn : Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn;
        }
    }
}