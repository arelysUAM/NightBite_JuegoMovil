package ni.edu.uam.nightbiteapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NightBiteColorScheme = darkColorScheme(
    primary = CheeseYellow,
    onPrimary = DarkText,

    secondary = NeonCyan,
    onSecondary = DarkText,

    tertiary = ParanormalGreen,
    onTertiary = DarkText,

    background = NightBackground,
    onBackground = SmokeWhite,

    surface = NightSurface,
    onSurface = SmokeWhite,

    primaryContainer = PizzaRed,
    onPrimaryContainer = SmokeWhite,

    secondaryContainer = NeonCyan,
    onSecondaryContainer = DarkText,

    outline = LavenderGray
)

@Composable
fun NightbiteAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NightBiteColorScheme,
        typography = Typography,
        content = content
    )
}