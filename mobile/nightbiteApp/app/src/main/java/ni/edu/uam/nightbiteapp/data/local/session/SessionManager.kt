package ni.edu.uam.nightbiteapp.data.local.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse

private val Context.sessionDataStore by preferencesDataStore(name = "user_session")

class SessionManager(
    private val context: Context
) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = longPreferencesKey("user_id")
        private val USERNAME = stringPreferencesKey("username")
        private val EMAIL = stringPreferencesKey("email")
        private val AGE = intPreferencesKey("age")
        private val HAS_PLAYER = booleanPreferencesKey("has_player")
        private val PLAYER_GENDER = stringPreferencesKey("player_gender")
        private val LAST_ACTIVE_TIME = longPreferencesKey("last_active_time")
    }

    val userSessionFlow: Flow<UserSession> = context.sessionDataStore.data
        .map { preferences ->
            UserSession(
                isLoggedIn = preferences[IS_LOGGED_IN] ?: false,
                userId = preferences[USER_ID],
                username = preferences[USERNAME] ?: "",
                email = preferences[EMAIL] ?: "",
                age = preferences[AGE],
                hasPlayer = preferences[HAS_PLAYER] ?: false,
                playerGender = preferences[PLAYER_GENDER] ?: ""
            )
        }

    suspend fun saveSession(
        user: UserResponse
    ) {
        context.sessionDataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = user.id
            preferences[USERNAME] = user.username
            preferences[EMAIL] = user.email
            preferences[AGE] = user.age
            preferences[HAS_PLAYER] = user.player != null
            preferences[PLAYER_GENDER] = user.player?.gender.orEmpty()
            preferences[LAST_ACTIVE_TIME] = System.currentTimeMillis()
        }
    }

    suspend fun markPlayerCreated(
        gender: String = ""
    ) {
        context.sessionDataStore.edit { preferences ->
            preferences[HAS_PLAYER] = true

            if (gender.isNotBlank()) {
                preferences[PLAYER_GENDER] = gender
            }

            preferences[LAST_ACTIVE_TIME] = System.currentTimeMillis()
        }
    }

    suspend fun updateLastActiveTime() {
        context.sessionDataStore.edit { preferences ->
            preferences[LAST_ACTIVE_TIME] = System.currentTimeMillis()
        }
    }

    suspend fun saveLastActiveTime(
        timeMillis: Long = System.currentTimeMillis()
    ) {
        context.sessionDataStore.edit { preferences ->
            preferences[LAST_ACTIVE_TIME] = timeMillis
        }
    }

    suspend fun getLastActiveTime(): Long {
        val preferences = context.sessionDataStore.data.first()

        return preferences[LAST_ACTIVE_TIME] ?: 0L
    }

    suspend fun shouldResetNavigationByInactivity(
        inactivityLimitMillis: Long
    ): Boolean {
        val lastActiveTime = getLastActiveTime()

        if (lastActiveTime <= 0L) {
            updateLastActiveTime()
            return false
        }

        val elapsedTime = System.currentTimeMillis() - lastActiveTime

        return elapsedTime >= inactivityLimitMillis
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}