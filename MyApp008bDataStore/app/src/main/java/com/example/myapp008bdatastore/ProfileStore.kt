package com.example.myapp008bdatastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Jedna instance DataStore pro celou aplikaci (soubor "gamer_profile")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "gamer_profile")

// Uložený profil hráče
data class Profile(val nickname: String, val hardcore: Boolean)

object ProfileStore {
    // Typované klíče
    private val NICKNAME_KEY = stringPreferencesKey("NICKNAME")
    private val HARDCORE_KEY = booleanPreferencesKey("HARDCORE")

    // Proud dat – při každé změně pošle nový profil
    fun profile(context: Context): Flow<Profile> = context.dataStore.data.map { prefs ->
        Profile(prefs[NICKNAME_KEY].orEmpty(), prefs[HARDCORE_KEY] ?: false)
    }

    // Asynchronní zápis (suspend funkce – volá se z coroutine)
    suspend fun save(context: Context, profile: Profile) {
        context.dataStore.edit { prefs ->
            prefs[NICKNAME_KEY] = profile.nickname
            prefs[HARDCORE_KEY] = profile.hardcore
        }
    }
}
