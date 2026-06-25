package ni.edu.uam.nightbiteapp.ui.model

import androidx.annotation.DrawableRes

data class LevelIntroContent(
    @DrawableRes val enemyImageRes: Int,
    val enemyTitle: String,
    val alertMessage: String,
    val difficulty: String,
    val behavior: String,
    val tip: String
)