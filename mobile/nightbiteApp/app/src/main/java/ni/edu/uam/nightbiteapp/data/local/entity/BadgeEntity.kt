package ni.edu.uam.nightbiteapp.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "badges",
    primaryKeys = ["userId", "levelId"]
)
data class BadgeEntity(
    val userId: Long,
    val levelId: Int,
    val badgeCode: String,
    val unlockedAt: Long = System.currentTimeMillis()
)