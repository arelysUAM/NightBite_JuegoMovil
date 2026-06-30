package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

@Composable
fun MoonTransitionScreen(
    message: String,
    framePrefix: String,
    firstFrame: Int = 27,
    lastFrame: Int = 1,
    durationMillis: Long = 3000L,
    onFinished: () -> Unit
) {
    val context = LocalContext.current

    val frames = remember(
        framePrefix,
        firstFrame,
        lastFrame
    ) {
        val range = if (firstFrame >= lastFrame) {
            firstFrame downTo lastFrame
        } else {
            firstFrame..lastFrame
        }

        range.mapNotNull { frameNumber ->
            context.resources.getIdentifier(
                "${framePrefix}$frameNumber",
                "drawable",
                context.packageName
            ).takeIf { resourceId ->
                resourceId != 0
            }
        }
    }

    var currentFrameIndex by remember(frames) {
        mutableIntStateOf(0)
    }

    LaunchedEffect(frames) {
        if (frames.isEmpty()) {
            delay(durationMillis)
            onFinished()
            return@LaunchedEffect
        }

        val frameDelay = (durationMillis / frames.size)
            .coerceAtLeast(1L)

        frames.indices.forEach { index ->
            currentFrameIndex = index
            delay(frameDelay)
        }

        onFinished()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF08061B)),
        contentAlignment = Alignment.Center
    ) {
        if (frames.isNotEmpty()) {
            Image(
                painter = painterResource(id = frames[currentFrameIndex]),
                contentDescription = message,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }

        Text(
            text = message,
            color = SmokeWhite,
            fontSize = if (maxHeight < 420.dp) 28.sp else 34.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                shadow = Shadow(
                    color = Color(0xFF7A4DFF),
                    offset = Offset(2f, 3f),
                    blurRadius = 6f
                )
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-34).dp)
        )
    }
}