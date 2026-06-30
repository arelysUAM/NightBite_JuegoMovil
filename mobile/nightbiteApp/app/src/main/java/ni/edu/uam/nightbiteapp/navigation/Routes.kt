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

    const val NORMAL_WORLD_RETURN = "normal_world_return"

    const val CHEESE_MOON_TRANSITION =
        "cheese_moon_transition/{levelId}"

    const val GENDER_SELECTION = "gender_selection"

    const val HOME = "home"
    const val SETTINGS = "settings"
    const val ACCOUNT = "account"
    const val PASSWORD = "password"

    const val PLAYER_CREATION = "player_creation"
    const val ACHIEVEMENTS = "achievements"

    const val TUTORIAL_LOADING = "tutorial_loading"
    const val TUTORIAL_GAME = "tutorial_game"

    const val LEVEL_INTRO = "level_intro/{levelId}"
    const val GAME_PLACEHOLDER = "game_placeholder/{levelId}"

    const val WANTED_POSTER_TRANSITION =
        "wanted_poster_transition/{levelId}/{resultType}/{stars}"

    const val GAME_RESULT = "game_result/{levelId}/{resultType}/{stars}"

    fun cheeseMoonTransition(
        levelId: Int
    ): String {
        return "cheese_moon_transition/${levelId.coerceIn(0, 4)}"
    }

    fun registerWithAge(
        age: Int
    ): String {
        return "register/${age.coerceAtLeast(13)}"
    }

    fun levelIntro(
        levelId: Int
    ): String {
        return "level_intro/${levelId.coerceIn(0, 4)}"
    }

    fun gamePlaceholder(
        levelId: Int
    ): String {
        return "game_placeholder/${levelId.coerceIn(0, 4)}"
    }

    fun wantedPosterTransition(
        levelId: Int,
        resultType: String,
        stars: Int
    ): String {
        return "wanted_poster_transition/${levelId.coerceIn(0, 4)}/$resultType/${stars.coerceIn(0, 3)}"
    }

    fun gameResult(
        levelId: Int,
        resultType: String,
        stars: Int
    ): String {
        return "game_result/${levelId.coerceIn(0, 4)}/$resultType/${stars.coerceIn(0, 3)}"
    }
}