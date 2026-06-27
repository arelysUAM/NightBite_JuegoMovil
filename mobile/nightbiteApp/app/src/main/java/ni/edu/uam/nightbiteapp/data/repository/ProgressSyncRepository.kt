package ni.edu.uam.nightbiteapp.data.repository

import kotlinx.coroutines.flow.first
import ni.edu.uam.nightbiteapp.data.local.dao.GameProgressDao
import ni.edu.uam.nightbiteapp.data.local.entity.BadgeEntity
import ni.edu.uam.nightbiteapp.data.local.entity.LevelResultEntity
import ni.edu.uam.nightbiteapp.data.local.entity.ProgressEntity
import ni.edu.uam.nightbiteapp.data.remote.ApiService
import ni.edu.uam.nightbiteapp.data.remote.dto.BadgeSyncRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.LevelResultProgressResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.LevelResultSyncRequest
import ni.edu.uam.nightbiteapp.data.remote.dto.ProgressResponse
import ni.edu.uam.nightbiteapp.data.remote.dto.ProgressSyncRequest


class ProgressSyncRepository(
    private val gameProgressDao: GameProgressDao,
    private val apiService: ApiService
) {

    suspend fun uploadLocalProgressToApi(userId: Long): Boolean {
        return try {
            val request = buildProgressSyncRequest(userId)

            val response = apiService.syncProgress(
                userAccountId = userId,
                request = request
            )

            if (!response.isSuccessful) {
                return false
            }

            val remoteProgress = response.body()
                ?: return false

            mergeRemoteProgressIntoRoom(
                userId = userId,
                remoteProgress = remoteProgress
            )

            true
        } catch (exception: Exception) {
            false
        }
    }

    suspend fun downloadRemoteProgressToRoom(userId: Long): Boolean {
        return try {
            val response = apiService.getProgressByUserAccountId(userId)

            if (!response.isSuccessful) {
                return false
            }

            val remoteProgress = response.body()
                ?: return false

            mergeRemoteProgressIntoRoom(
                userId = userId,
                remoteProgress = remoteProgress
            )

            true
        } catch (exception: Exception) {
            false
        }
    }

    suspend fun syncProgress(userId: Long): Boolean {
        val downloaded = downloadRemoteProgressToRoom(userId)

        val uploaded = uploadLocalProgressToApi(userId)

        return downloaded || uploaded
    }

    private suspend fun buildProgressSyncRequest(
        userId: Long
    ): ProgressSyncRequest {
        val progress = gameProgressDao.getProgress(userId)

        val levelResults = gameProgressDao
            .observeLevelResults(userId)
            .first()

        val badges = gameProgressDao
            .observeBadges(userId)
            .first()

        return ProgressSyncRequest(
            maxUnlockedLevel = progress
                ?.maxUnlockedLevel
                ?.coerceIn(0, MAX_LEVEL_ID)
                ?: 0,
            levelResults = levelResults.map { result ->
                LevelResultSyncRequest(
                    levelId = result.levelId.coerceIn(0, MAX_LEVEL_ID),
                    bestStars = result.bestStars.coerceIn(0, REQUIRED_STARS_FOR_BADGE),
                    bestScore = result.bestScore.coerceAtLeast(0),
                    completedOrders = result.completedOrders.coerceAtLeast(0),
                    totalOrders = result.totalOrders.coerceAtLeast(0),
                    resultType = result.resultType.ifBlank {
                        UNKNOWN_RESULT_TYPE
                    },
                    elapsedTimeSeconds = result.elapsedTimeSeconds.coerceAtLeast(0f),
                    averageDeliveryTimeSeconds = result.averageDeliveryTimeSeconds.coerceAtLeast(0f)
                )
            },
            badges = badges.map { badge ->
                BadgeSyncRequest(
                    levelId = badge.levelId.coerceIn(0, MAX_LEVEL_ID),
                    badgeCode = badge.badgeCode.ifBlank {
                        badgeCodeForLevel(badge.levelId)
                    }
                )
            }
        )
    }

    private suspend fun mergeRemoteProgressIntoRoom(
        userId: Long,
        remoteProgress: ProgressResponse
    ) {
        mergeProgress(
            userId = userId,
            remoteMaxUnlockedLevel = remoteProgress.maxUnlockedLevel
        )

        remoteProgress.levelResults.forEach { remoteResult ->
            mergeLevelResult(
                userId = userId,
                remoteResult = remoteResult
            )
        }

        remoteProgress.badges.forEach { remoteBadge ->
            val levelId = remoteBadge.levelId.coerceIn(0, MAX_LEVEL_ID)

            val existingBadge = gameProgressDao.getBadge(
                userId = userId,
                levelId = levelId
            )

            if (existingBadge == null) {
                gameProgressDao.upsertBadge(
                    BadgeEntity(
                        userId = userId,
                        levelId = levelId,
                        badgeCode = remoteBadge.badgeCode.ifBlank {
                            badgeCodeForLevel(levelId)
                        }
                    )
                )
            }
        }
    }

    private suspend fun mergeProgress(
        userId: Long,
        remoteMaxUnlockedLevel: Int
    ) {
        val localProgress = gameProgressDao.getProgress(userId)

        val localMaxUnlockedLevel = localProgress
            ?.maxUnlockedLevel
            ?.coerceIn(0, MAX_LEVEL_ID)
            ?: 0

        val mergedMaxUnlockedLevel = maxOf(
            localMaxUnlockedLevel,
            remoteMaxUnlockedLevel.coerceIn(0, MAX_LEVEL_ID)
        )

        gameProgressDao.upsertProgress(
            ProgressEntity(
                userId = userId,
                maxUnlockedLevel = mergedMaxUnlockedLevel
            )
        )
    }

    private suspend fun mergeLevelResult(
        userId: Long,
        remoteResult: LevelResultProgressResponse
    ) {
        val levelId = remoteResult.levelId.coerceIn(0, MAX_LEVEL_ID)

        val localResult = gameProgressDao.getLevelResult(
            userId = userId,
            levelId = levelId
        )

        val remoteStars = remoteResult.bestStars.coerceIn(
            0,
            REQUIRED_STARS_FOR_BADGE
        )

        val remoteScore = remoteResult.bestScore.coerceAtLeast(0)

        val shouldSaveRemoteResult =
            localResult == null ||
                    remoteStars > localResult.bestStars ||
                    remoteStars == localResult.bestStars &&
                    remoteScore > localResult.bestScore

        if (shouldSaveRemoteResult) {
            gameProgressDao.upsertLevelResult(
                LevelResultEntity(
                    userId = userId,
                    levelId = levelId,
                    bestStars = remoteStars,
                    bestScore = remoteScore,
                    completedOrders = remoteResult.completedOrders.coerceAtLeast(0),
                    totalOrders = remoteResult.totalOrders.coerceAtLeast(0),
                    resultType = remoteResult.resultType.ifBlank {
                        UNKNOWN_RESULT_TYPE
                    },
                    elapsedTimeSeconds = remoteResult.elapsedTimeSeconds.coerceAtLeast(0f),
                    averageDeliveryTimeSeconds = remoteResult.averageDeliveryTimeSeconds.coerceAtLeast(0f)
                )
            )
        }

        if (remoteStars == REQUIRED_STARS_FOR_BADGE) {
            ensureBadgeExists(
                userId = userId,
                levelId = levelId
            )
        }
    }

    private suspend fun ensureBadgeExists(
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

    companion object {
        private const val MAX_LEVEL_ID = 4
        private const val REQUIRED_STARS_FOR_BADGE = 3
        private const val UNKNOWN_RESULT_TYPE = "UNKNOWN_RESULT"
    }
}