package ni.edu.uam.nightbiteapp.ui.model

data class NightLevel(
    val id: Int,
    val title: String,
    val subtitle: String,
    val narrativeMessage: String,
    val enemyName: String,
    val enemyDescription: String,
    val enemyBehavior: String,
    val survivalTip: String,
    val isUnlocked: Boolean = true,
    val stars: Int = 0
)