package ni.edu.uam.nightbiteapp.ui.model

data class NightLevel(
    val id: Int,
    val title: String,
    val subtitle: String,
    val narrativeMessage: String,
    val introContent: LevelIntroContent,
    val isUnlocked: Boolean = true,
    val stars: Int = 0,

    // Se dejan por compatibilidad si algún archivo viejo todavía los usa.
    val enemyName: String = introContent.enemyTitle,
    val enemyDescription: String = "",
    val enemyBehavior: String = introContent.behavior,
    val survivalTip: String = introContent.tip
)