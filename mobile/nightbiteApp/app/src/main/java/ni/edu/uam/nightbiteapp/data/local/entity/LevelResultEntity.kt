package ni.edu.uam.nightbiteapp.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "level_results",
    primaryKeys = ["userId", "levelId"]
)
data class LevelResultEntity(
    val userId: Long,
    val levelId: Int,
    val bestStars: Int,
    val bestScore: Int,
    val completedOrders: Int,
    val totalOrders: Int,
    val resultType: String,
    val elapsedTimeSeconds: Float,
    val averageDeliveryTimeSeconds: Float,
    val updatedAt: Long = System.currentTimeMillis()
)