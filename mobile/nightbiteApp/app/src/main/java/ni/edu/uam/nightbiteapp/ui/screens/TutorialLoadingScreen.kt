package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.branding.GameTitle
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.scale

private const val TUTORIAL_LOADING_TOTAL_TIME = 3000L
private const val LOADING_DOTS_INTERVAL = 450L

@Composable
fun TutorialLoadingScreen(
    onTutorialLoaded: () -> Unit
) {
    BackHandler {
        // Se bloquea el botón atrás durante la transición.
    }

    LaunchedEffect(Unit) {
        delay(TUTORIAL_LOADING_TOTAL_TIME)
        onTutorialLoaded()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.thumbnail2),
            contentDescription = "Cargando tutorial",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart
        )

        val infiniteTransition = rememberInfiniteTransition(
            label = "TutorialLogoBreathing"
        )

        val logoScale by infiniteTransition.animateFloat(
            initialValue = 0.96f,
            targetValue = 1.04f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1400),
                repeatMode = RepeatMode.Reverse
            ),
            label = "TutorialLogoScale"
        )

        GameTitle(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = NightSizes.registerCardWidth)
                .scale(logoScale)
        )

        TutorialLoadingText()
    }
}

@Composable
private fun BoxScope.TutorialLoadingText() {
    var dots by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(LOADING_DOTS_INTERVAL)
            dots = (dots + 1) % 4
        }
    }

    val loadingText = "Cargando tutorial" + ".".repeat(dots)

    Text(
        text = loadingText,
        textAlign = TextAlign.Center,
        fontFamily = LilitaOne,
        style = MaterialTheme.typography.titleLarge.copy(
            color = SmokeWhite,
            shadow = Shadow(
                color = NightSurface,
                offset = Offset(3f, 3f),
                blurRadius = 4f
            )
        ),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = NightSpacing.section)
    )
}