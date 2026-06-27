package ni.edu.uam.nightbiteapp.data.remote.dto

data class ProgressSyncRequest(
    val maxUnlockedLevel: Int,
    val levelResults: List<LevelResultSyncRequest>,
    val badges: List<BadgeSyncRequest>
)