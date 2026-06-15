package ni.edu.uam.nightbiteapp.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.model.NightLevel
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.NeonCyan
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

@Composable
fun NightLevelButton(
    level: NightLevel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mainColor = if (level.isUnlocked) {
        CheeseYellow
    } else {
        Color(0xFFBDBDBD)
    }

    val shadowColor = if (level.isUnlocked) {
        Color(0xFFFFA000)
    } else {
        Color(0xFF6F7578)
    }

    val levelNumber = level.id + 1

    Box(
        modifier = modifier.size(NightSizes.levelButtonContainer),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(NightSizes.levelButton)
                .offset(x = 10.dp, y = 10.dp)
                .background(
                    color = shadowColor,
                    shape = NightShapes.levelButton
                )
        )

        Box(
            modifier = Modifier
                .size(NightSizes.levelButton)
                .shadow(
                    elevation = 6.dp,
                    shape = NightShapes.levelButton
                )
                .background(
                    color = mainColor,
                    shape = NightShapes.levelButton
                )
                .then(
                    if (level.isUnlocked) {
                        Modifier.clickable { onClick() }
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = levelNumber.toString(),
                color = SmokeWhite,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }

        if (!level.isUnlocked) {
            LockedLevelBadge(
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun LockedLevelBadge(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(
                color = Color(0xFF17345A),
                shape = CircleShape
            )
            .shadow(
                elevation = 4.dp,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = NeonCyan,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Nivel bloqueado",
                tint = DarkText,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}