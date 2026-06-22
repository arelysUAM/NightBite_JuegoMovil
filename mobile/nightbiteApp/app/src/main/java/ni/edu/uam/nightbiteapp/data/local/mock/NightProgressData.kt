package ni.edu.uam.nightbiteapp.data.local.mock

object NightProgressData {
    private val unlockedLevelByUser = mutableMapOf<Long, Int>()
    private val levelStarsByUser = mutableMapOf<Long, MutableMap<Int, Int>>()

    fun getMaxUnlockedLevel(userId: Long?): Int {
        if (userId == null) return 0
        return unlockedLevelByUser[userId] ?: 0
    }

    fun getLevelStars(
        userId: Long?,
        levelId: Int
    ): Int {
        if (userId == null) return 0

        return levelStarsByUser[userId]
            ?.get(levelId)
            ?: 0
    }

    fun getStarsByLevel(userId: Long?): Map<Int, Int> {
        if (userId == null) return emptyMap()

        return levelStarsByUser[userId]
            ?.toMap()
            ?: emptyMap()
    }

    fun saveLevelStars(
        userId: Long?,
        levelId: Int,
        stars: Int
    ) {
        if (userId == null) return

        val safeLevelId = levelId.coerceIn(0, 4)
        val safeStars = stars.coerceIn(0, 3)

        val userStars = levelStarsByUser.getOrPut(userId) {
            mutableMapOf()
        }

        val currentStars = userStars[safeLevelId] ?: 0

        if (safeStars > currentStars) {
            userStars[safeLevelId] = safeStars
        }
    }

    fun unlockNextLevel(
        userId: Long?,
        completedLevelId: Int
    ) {
        if (userId == null) return

        val currentMaxUnlocked = getMaxUnlockedLevel(userId)
        val nextLevelId = completedLevelId + 1

        if (nextLevelId > currentMaxUnlocked) {
            unlockedLevelByUser[userId] = nextLevelId.coerceAtMost(4)
        }
    }
}