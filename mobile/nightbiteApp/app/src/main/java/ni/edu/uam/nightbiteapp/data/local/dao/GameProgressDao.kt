package ni.edu.uam.nightbiteapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ni.edu.uam.nightbiteapp.data.local.entity.BadgeEntity
import ni.edu.uam.nightbiteapp.data.local.entity.LevelResultEntity
import ni.edu.uam.nightbiteapp.data.local.entity.ProgressEntity

@Dao
interface GameProgressDao {

    @Query("SELECT * FROM progress WHERE userId = :userId LIMIT 1")
    fun observeProgress(userId: Long): Flow<ProgressEntity?>

    @Query("SELECT * FROM progress WHERE userId = :userId LIMIT 1")
    suspend fun getProgress(userId: Long): ProgressEntity?

    @Upsert
    suspend fun upsertProgress(progress: ProgressEntity)

    @Query("SELECT * FROM level_results WHERE userId = :userId ORDER BY levelId ASC")
    fun observeLevelResults(userId: Long): Flow<List<LevelResultEntity>>

    @Query(
        """
        SELECT * FROM level_results
        WHERE userId = :userId AND levelId = :levelId
        LIMIT 1
        """
    )
    suspend fun getLevelResult(
        userId: Long,
        levelId: Int
    ): LevelResultEntity?

    @Upsert
    suspend fun upsertLevelResult(result: LevelResultEntity)

    @Query("SELECT * FROM badges WHERE userId = :userId ORDER BY levelId ASC")
    fun observeBadges(userId: Long): Flow<List<BadgeEntity>>

    @Query(
        """
        SELECT * FROM badges
        WHERE userId = :userId AND levelId = :levelId
        LIMIT 1
        """
    )
    suspend fun getBadge(
        userId: Long,
        levelId: Int
    ): BadgeEntity?

    @Upsert
    suspend fun upsertBadge(badge: BadgeEntity)
}