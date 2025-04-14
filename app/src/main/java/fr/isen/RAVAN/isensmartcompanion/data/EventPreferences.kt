package fr.isen.RAVAN.isensmartcompanion.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Nom du fichier DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "event_preferences")

class EventPreferences(private val context: Context) {

    // Fonction pour sauvegarder l'état de la notification pour un événement
    suspend fun saveEventNotificationStatus(eventId: String, isNotified: Boolean) {
        val preferencesKey = booleanPreferencesKey(eventId)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = isNotified
        }
    }

    // Fonction pour récupérer l'état de la notification pour un événement
    fun getEventNotificationStatus(eventId: String): Flow<Boolean> {
        val preferencesKey = booleanPreferencesKey(eventId)
        return context.dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: false // Retourne false si la préférence n'existe pas
        }
    }
}
