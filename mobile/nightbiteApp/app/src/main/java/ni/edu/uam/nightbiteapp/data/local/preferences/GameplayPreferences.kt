package ni.edu.uam.nightbiteapp.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ControlsLayout {
    DIRECTIONS_RIGHT,
    DIRECTIONS_LEFT
}

private val Context.gameplayPreferencesDataStore by preferencesDataStore(
    name = "gameplay_preferences"
)

object GameplayPreferences {

    val DEFAULT_CONTROLS_LAYOUT = ControlsLayout.DIRECTIONS_RIGHT

    private val DIRECTIONS_ON_RIGHT_KEY = booleanPreferencesKey(
        name = "directions_on_right"
    )

    fun controlsLayoutFlow(
        context: Context
    ): Flow<ControlsLayout> {
        return context.applicationContext.gameplayPreferencesDataStore.data.map { preferences ->
            val directionsOnRight = preferences[DIRECTIONS_ON_RIGHT_KEY] ?: true

            if (directionsOnRight) {
                ControlsLayout.DIRECTIONS_RIGHT
            } else {
                ControlsLayout.DIRECTIONS_LEFT
            }
        }
    }

    suspend fun setControlsLayout(
        context: Context,
        controlsLayout: ControlsLayout
    ) {
        context.applicationContext.gameplayPreferencesDataStore.edit { preferences ->
            preferences[DIRECTIONS_ON_RIGHT_KEY] =
                controlsLayout == ControlsLayout.DIRECTIONS_RIGHT
        }
    }

    suspend fun resetControlsLayoutToDefault(
        context: Context
    ) {
        setControlsLayout(
            context = context,
            controlsLayout = DEFAULT_CONTROLS_LAYOUT
        )
    }
}