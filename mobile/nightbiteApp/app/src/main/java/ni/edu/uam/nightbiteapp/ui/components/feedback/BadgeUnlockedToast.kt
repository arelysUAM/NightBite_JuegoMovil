package ni.edu.uam.nightbiteapp.ui.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

data class BadgeUnlockedNotification(
    val levelId: Int,
    val title: String = "Insignia desbloqueada",
    val message: String
)

@Composable
fun BadgeUnlockedToast(
    notification: BadgeUnlockedNotification,
    modifier: Modifier = Modifier,
    onFinished: () -> Unit
) {
    var isVisible by remember(notification) {
        mutableStateOf(false)
    }

    LaunchedEffect(notification) {
        isVisible = true

        // 380 ms entrada + 2240 ms visible + 380 ms salida = 3000 ms aprox.
        delay(3000)

        isVisible = false

        delay(380)

        onFinished()
    }

    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth ->
                fullWidth
            },
            animationSpec = tween(
                durationMillis = 380,
                easing = FastOutSlowInEasing
            )
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth ->
                fullWidth
            },
            animationSpec = tween(
                durationMillis = 380,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        Row(
            modifier = Modifier
                .widthIn(
                    min = 300.dp,
                    max = 440.dp
                )
                .background(
                    color = Color.White.copy(alpha = 0.96f),
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        bottomStart = 50.dp
                    )
                )
                .padding(
                    start = 12.dp,
                    top = 8.dp,
                    end = 18.dp,
                    bottom = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BadgeIcon()

            Box(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = notification.title,
                        color = Color(0xFFF7A928),
                        fontFamily = LilitaOne,
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.White.copy(alpha = 0.65f),
                                blurRadius = 2f
                            )
                        )
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                color = CheeseYellow.copy(alpha = 0.28f),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(
                                horizontal = 14.dp,
                                vertical = 4.dp
                            )
                    ) {
                        Text(
                            text = notification.message,
                            color = Color(0xFFF0A322),
                            fontFamily = LilitaOne,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgeIcon() {
    Box(
        modifier = Modifier
            .size(58.dp)
            .background(
                color = Color(0xFFFFB800),
                shape = CircleShape
            )
            .border(
                width = 5.dp,
                color = SmokeWhite,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Insignia",
            tint = SmokeWhite,
            modifier = Modifier.size(30.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 13.dp)
                .size(
                    width = 24.dp,
                    height = 24.dp
                )
                .background(
                    color = Color(0xFFFFB800),
                    shape = RoundedCornerShape(
                        bottomStart = 6.dp,
                        bottomEnd = 6.dp
                    )
                )
        )
    }
}