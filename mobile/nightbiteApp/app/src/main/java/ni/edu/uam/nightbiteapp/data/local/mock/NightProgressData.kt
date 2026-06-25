package ni.edu.uam.nightbiteapp.data.local.mock

/**
 * Fuente temporal de progreso del juego.
 *
 * Por ahora el progreso se guarda en memoria mientras no se implemente Room.
 *
 * Reglas:
 * - El nivel 0 siempre está disponible.
 * - Las estrellas guardadas por nivel nunca disminuyen.
 * - El siguiente nivel solo se desbloquea cuando AppNavigation confirma
 *   que el resultado fue perfecto, es decir, 3 estrellas.
 * - El nivel máximo disponible es el 4.
 */
object NightProgressData {
    private val unlockedLevelByUser = mutableMapOf<Long, Int>()
    private val levelStarsByUser = mutableMapOf<Long, MutableMap<Int, Int>>()

    fun getMaxUnlockedLevel(
        userId: Long?
    ): Int {
        if (userId == null) return MIN_LEVEL_ID

        return unlockedLevelByUser[userId]
            ?.coerceIn(MIN_LEVEL_ID, MAX_LEVEL_ID)
            ?: MIN_LEVEL_ID
    }

    fun getLevelStars(
        userId: Long?,
        levelId: Int
    ): Int {
        if (userId == null) return MIN_STARS

        val safeLevelId = levelId.coerceIn(
            minimumValue = MIN_LEVEL_ID,
            maximumValue = MAX_LEVEL_ID
        )

        return levelStarsByUser[userId]
            ?.get(safeLevelId)
            ?.coerceIn(MIN_STARS, MAX_STARS)
            ?: MIN_STARS
    }

    fun getStarsByLevel(
        userId: Long?
    ): Map<Int, Int> {
        if (userId == null) return emptyMap()

        return levelStarsByUser[userId]
            ?.mapValues { entry ->
                entry.value.coerceIn(MIN_STARS, MAX_STARS)
            }
            ?: emptyMap()
    }

    fun saveLevelStars(
        userId: Long?,
        levelId: Int,
        stars: Int
    ) {
        if (userId == null) return

        val safeLevelId = levelId.coerceIn(
            minimumValue = MIN_LEVEL_ID,
            maximumValue = MAX_LEVEL_ID
        )

        val safeStars = stars.coerceIn(
            minimumValue = MIN_STARS,
            maximumValue = MAX_STARS
        )

        val userStars = levelStarsByUser.getOrPut(userId) {
            mutableMapOf()
        }

        val currentStars = userStars[safeLevelId] ?: MIN_STARS

        if (safeStars > currentStars) {
            userStars[safeLevelId] = safeStars
        }
    }

    fun unlockNextLevel(
        userId: Long?,
        completedLevelId: Int
    ) {
        if (userId == null) return

        val safeCompletedLevelId = completedLevelId.coerceIn(
            minimumValue = MIN_LEVEL_ID,
            maximumValue = MAX_LEVEL_ID
        )

        val nextLevelId = (safeCompletedLevelId + 1).coerceAtMost(
            maximumValue = MAX_LEVEL_ID
        )

        val currentMaxUnlockedLevel = getMaxUnlockedLevel(userId)

        if (nextLevelId > currentMaxUnlockedLevel) {
            unlockedLevelByUser[userId] = nextLevelId
        }
    }

    fun resetUserProgress(
        userId: Long?
    ) {
        if (userId == null) return

        unlockedLevelByUser[userId] = MIN_LEVEL_ID
        levelStarsByUser.remove(userId)
    }

    private const val MIN_LEVEL_ID = 0
    private const val MAX_LEVEL_ID = 4

    private const val MIN_STARS = 0
    private const val MAX_STARS = 3
}