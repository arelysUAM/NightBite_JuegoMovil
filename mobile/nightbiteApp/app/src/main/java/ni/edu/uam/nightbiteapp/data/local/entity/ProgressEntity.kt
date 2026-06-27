package ni.edu.uam.nightbiteapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class ProgressEntity(
    @PrimaryKey
    val userId: Long,
    val maxUnlockedLevel: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)