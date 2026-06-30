package ni.edu.uam.nightbiteapp.ui.model

enum class GameResultType(
    val defaultStars: Int,
    val isTutorialResult: Boolean,
    val isFinalLevelResult: Boolean,
    val isWinResult: Boolean,
    val isLossResult: Boolean
) {
    /*
     * Nivel 0 - Tutorial
     */
    TUTORIAL_ALL_DELIVERIES(
        defaultStars = 3,
        isTutorialResult = true,
        isFinalLevelResult = false,
        isWinResult = true,
        isLossResult = false
    ),

    TUTORIAL_EIGHTY_PERCENT(
        defaultStars = 2,
        isTutorialResult = true,
        isFinalLevelResult = false,
        isWinResult = false,
        isLossResult = false
    ),

    TUTORIAL_HALF_DELIVERIES(
        defaultStars = 1,
        isTutorialResult = true,
        isFinalLevelResult = false,
        isWinResult = false,
        isLossResult = false
    ),

    TUTORIAL_FIRED(
        defaultStars = 0,
        isTutorialResult = true,
        isFinalLevelResult = false,
        isWinResult = false,
        isLossResult = true
    ),

    TUTORIAL_TIME_EXPIRED(
        defaultStars = 0,
        isTutorialResult = true,
        isFinalLevelResult = false,
        isWinResult = false,
        isLossResult = true
    ),

    /*
     * Niveles 1, 2 y 3
     */
    LEVEL_WIN(
        defaultStars = 3,
        isTutorialResult = false,
        isFinalLevelResult = false,
        isWinResult = true,
        isLossResult = false
    ),

    LEVEL_OUT_OF_LIVES(
        defaultStars = 0,
        isTutorialResult = false,
        isFinalLevelResult = false,
        isWinResult = false,
        isLossResult = true
    ),

    LEVEL_INCOMPLETE_AT_LEAST_50(
        defaultStars = 2,
        isTutorialResult = false,
        isFinalLevelResult = false,
        isWinResult = false,
        isLossResult = true
    ),

    LEVEL_INCOMPLETE_UNDER_50(
        defaultStars = 1,
        isTutorialResult = false,
        isFinalLevelResult = false,
        isWinResult = false,
        isLossResult = true
    ),

    LEVEL_TIME_EXPIRED(
        defaultStars = 0,
        isTutorialResult = false,
        isFinalLevelResult = false,
        isWinResult = false,
        isLossResult = true
    ),

    /*
     * Nivel 4 - Final
     */
    FINAL_WIN(
        defaultStars = 3,
        isTutorialResult = false,
        isFinalLevelResult = true,
        isWinResult = true,
        isLossResult = false
    ),

    FINAL_OUT_OF_LIVES(
        defaultStars = 0,
        isTutorialResult = false,
        isFinalLevelResult = true,
        isWinResult = false,
        isLossResult = true
    ),

    FINAL_INCOMPLETE_AT_LEAST_50(
        defaultStars = 2,
        isTutorialResult = false,
        isFinalLevelResult = true,
        isWinResult = false,
        isLossResult = true
    ),

    FINAL_INCOMPLETE_UNDER_50(
        defaultStars = 1,
        isTutorialResult = false,
        isFinalLevelResult = true,
        isWinResult = false,
        isLossResult = true
    ),

    FINAL_TIME_EXPIRED(
        defaultStars = 0,
        isTutorialResult = false,
        isFinalLevelResult = true,
        isWinResult = false,
        isLossResult = true
    );

    val unlocksNextLevel: Boolean
        get() = isWinResult && defaultStars == 3 && !isFinalLevelResult

    val shouldSaveStars: Boolean
        get() = defaultStars > 0 || isWinResult

    val shouldShowContinueButton: Boolean
        get() = isWinResult

    val shouldShowRetryButton: Boolean
        get() = !isWinResult
}