package ni.edu.uam.nightbiteapp.navigation

/**
 * Objeto que centraliza las rutas de navegación de la aplicación.
 *
 * Mantener las rutas en un solo archivo evita escribir textos repetidos
 * directamente en las pantallas o en el NavHost.
 */
object Routes {
    const val START = "start"
    const val LOGIN = "login"
    const val AGE_CHECK = "age_check"
    const val REGISTER = "register"
    const val REGISTER_WITH_AGE = "register/{age}"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val GAME_PLACEHOLDER = "game_placeholder"
    const val PLAYER_CREATION = "player_creation"

    fun registerWithAge(age: Int): String {
        return "register/$age"
    }
}