package ni.edu.uam.nightbiteapp.data.local.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse

private val Context.sessionDataStore by preferencesDataStore(name = "user_session")

/**
 * Clase encargada de guardar, leer y limpiar la sesión local del usuario.
 *
 * Usa Preferences DataStore porque solo necesitamos almacenar datos pequeños
 * como el id del usuario, username, email, token y estado de sesión.
 */
class SessionManager(
    private val context: Context
) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = longPreferencesKey("user_id")
        private val USERNAME = stringPreferencesKey("username")
        private val EMAIL = stringPreferencesKey("email")
        private val AGE = intPreferencesKey("age")
        private val TOKEN = stringPreferencesKey("token")
    }

    val userSessionFlow: Flow<UserSession> = context.sessionDataStore.data
        .map { preferences ->
            UserSession(
                isLoggedIn = preferences[IS_LOGGED_IN] ?: false,
                userId = preferences[USER_ID],
                username = preferences[USERNAME] ?: "",
                email = preferences[EMAIL] ?: "",
                age = preferences[AGE],
                token = preferences[TOKEN] ?: ""
            )
        }

    suspend fun saveSession(
        user: UserResponse,
        token: String
    ) {
        context.sessionDataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = user.id
            preferences[USERNAME] = user.username
            preferences[EMAIL] = user.email
            preferences[AGE] = user.age
            preferences[TOKEN] = token
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}