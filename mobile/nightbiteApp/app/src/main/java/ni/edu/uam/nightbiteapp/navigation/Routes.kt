package ni.edu.uam.nightbiteapp.navigation

/**
 * Objeto que centraliza las rutas de navegación de la aplicación.
 */
object Routes {
    const val START = "start"
    const val LOGIN = "login"
    const val AGE_CHECK = "age_check"
    const val REGISTER = "register"
    const val REGISTER_WITH_AGE = "register/{age}"

    const val GENDER_SELECTION = "gender_selection"

    const val HOME = "home"

    const val SETTINGS = "settings"
    const val ACCOUNT = "account"

    const val PASSWORD = "password"

    const val PLAYER_CREATION = "player_creation"
    const val PLAYER_DETAIL = "player_detail"

    const val LEVEL_INTRO = "level_intro/{levelId}"
    const val GAME_PLACEHOLDER = "game_placeholder/{levelId}"
    const val GAME_RESULT = "game_result/{levelId}/{resultType}"

    fun registerWithAge(age: Int): String {
        return "register/$age"
    }

    fun levelIntro(levelId: Int): String {
        return "level_intro/$levelId"
    }

    fun gamePlaceholder(levelId: Int): String {
        return "game_placeholder/$levelId"
    }

    fun gameResult(
        levelId: Int,
        resultType: String
    ): String {
        return "game_result/$levelId/$resultType"
    }
}