package ni.edu.uam.nightbiteapp.data.local.mock

object NightProgressData {
    private val unlockedLevelByUser = mutableMapOf<Long, Int>()
    private val tutorialStarsByUser = mutableMapOf<Long, Int>()

    fun getMaxUnlockedLevel(userId: Long?): Int {
        if (userId == null) return 0
        return unlockedLevelByUser[userId] ?: 0
    }

    fun getTutorialStars(userId: Long?): Int {
        if (userId == null) return 0
        return tutorialStarsByUser[userId] ?: 0
    }

    fun saveTutorialStars(
        userId: Long?,
        stars: Int
    ) {
        if (userId == null) return

        val safeStars = stars.coerceIn(0, 3)
        val currentStars = getTutorialStars(userId)

        if (safeStars > currentStars) {
            tutorialStarsByUser[userId] = safeStars
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