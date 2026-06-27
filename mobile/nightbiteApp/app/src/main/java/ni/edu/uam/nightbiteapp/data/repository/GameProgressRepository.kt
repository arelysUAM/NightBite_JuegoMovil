package ni.edu.uam.nightbiteapp.data.repository

import kotlinx.coroutines.flow.Flow
import ni.edu.uam.nightbiteapp.data.local.dao.GameProgressDao
import ni.edu.uam.nightbiteapp.data.local.entity.BadgeEntity
import ni.edu.uam.nightbiteapp.data.local.entity.LevelResultEntity
import ni.edu.uam.nightbiteapp.data.local.entity.ProgressEntity

class GameProgressRepository(
    private val gameProgressDao: GameProgressDao
) {

    fun observeProgress(userId: Long): Flow<ProgressEntity?> {
        return gameProgressDao.observeProgress(userId)
    }

    fun observeLevelResults(userId: Long): Flow<List<LevelResultEntity>> {
        return gameProgressDao.observeLevelResults(userId)
    }

    fun observeBadges(userId: Long): Flow<List<BadgeEntity>> {
        return gameProgressDao.observeBadges(userId)
    }

    suspend fun saveLevelResult(
        userId: Long,
        levelId: Int,
        stars: Int,
        score: Int,
        completedOrders: Int,
        totalOrders: Int,
        resultType: String,
        elapsedTimeSeconds: Float,
        averageDeliveryTimeSeconds: Float
    ) {
        val safeStars = stars.coerceIn(0, 3)

        val previousResult = gameProgressDao.getLevelResult(
            userId = userId,
            levelId = levelId
        )

        val bestStars = maxOf(
            previousResult?.bestStars ?: 0,
            safeStars
        )

        val bestScore = maxOf(
            previousResult?.bestScore ?: 0,
            score
        )

        val resultToSave = LevelResultEntity(
            userId = userId,
            levelId = levelId,
            bestStars = bestStars,
            bestScore = bestScore,
            completedOrders = completedOrders,
            totalOrders = totalOrders,
            resultType = resultType,
            elapsedTimeSeconds = elapsedTimeSeconds,
            averageDeliveryTimeSeconds = averageDeliveryTimeSeconds
        )

        gameProgressDao.upsertLevelResult(resultToSave)

        saveUnlockedProgress(
            userId = userId,
            completedLevelId = levelId,
            stars = safeStars
        )

        if (safeStars == 3) {
            unlockBadge(
                userId = userId,
                levelId = levelId
            )
        }
    }

    private suspend fun saveUnlockedProgress(
        userId: Long,
        completedLevelId: Int,
        stars: Int
    ) {
        val previousProgress = gameProgressDao.getProgress(userId)

        val currentMaxUnlockedLevel =
            previousProgress?.maxUnlockedLevel ?: 0

        val newMaxUnlockedLevel =
            if (stars == 3) {
                maxOf(
                    currentMaxUnlockedLevel,
                    completedLevelId + 1
                ).coerceAtMost(MAX_LEVEL_ID)
            } else {
                currentMaxUnlockedLevel
            }

        gameProgressDao.upsertProgress(
            ProgressEntity(
                userId = userId,
                maxUnlockedLevel = newMaxUnlockedLevel
            )
        )
    }

    private suspend fun unlockBadge(
        userId: Long,
        levelId: Int
    ) {
        val existingBadge = gameProgressDao.getBadge(
            userId = userId,
            levelId = levelId
        )

        if (existingBadge != null) return

        gameProgressDao.upsertBadge(
            BadgeEntity(
                userId = userId,
                levelId = levelId,
                badgeCode = badgeCodeForLevel(levelId)
            )
        )
    }

    companion object {

        private const val MAX_LEVEL_ID = 4

        private fun badgeCodeForLevel(levelId: Int): String {
            return when (levelId) {
                0 -> "tutorial_badge"
                1 -> "shadow_wanderer_badge"
                2 -> "spectral_dogs_badge"
                3 -> "deformed_clients_badge"
                4 -> "lost_delivery_badge"
                else -> "unknown_badge"
            }
        }
    }
}