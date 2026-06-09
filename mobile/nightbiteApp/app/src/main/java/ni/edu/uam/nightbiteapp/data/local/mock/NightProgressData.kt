package ni.edu.uam.nightbiteapp.data.local.mock

object NightProgressData {

    private val unlockedLevelByUser = mutableMapOf<Long, Int>()

    fun getMaxUnlockedLevel(userId: Long?): Int {
        if (userId == null) return 0

        return unlockedLevelByUser[userId] ?: 0
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