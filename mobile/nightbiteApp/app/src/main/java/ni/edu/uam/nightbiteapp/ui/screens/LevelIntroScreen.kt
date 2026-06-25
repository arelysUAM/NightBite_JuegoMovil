package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.model.LevelIntroContent
import ni.edu.uam.nightbiteapp.ui.model.NightLevel
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.IntroDataBorder
import ni.edu.uam.nightbiteapp.ui.theme.IntroEnemyBg
import ni.edu.uam.nightbiteapp.ui.theme.IntroHeaderPurple
import ni.edu.uam.nightbiteapp.ui.theme.IntroHeaderShadow
import ni.edu.uam.nightbiteapp.ui.theme.IntroInfoWhite
import ni.edu.uam.nightbiteapp.ui.theme.IntroLeftPurple
import ni.edu.uam.nightbiteapp.ui.theme.IntroOuterPurple
import ni.edu.uam.nightbiteapp.ui.theme.IntroRightPurple
import ni.edu.uam.nightbiteapp.ui.theme.IntroTextDark
import ni.edu.uam.nightbiteapp.ui.theme.IntroTextPurple
import ni.edu.uam.nightbiteapp.ui.theme.IntroWarningBg
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

@Composable
fun LevelIntroScreen(
    level: NightLevel?,
    onStartLevel: () -> Unit,
    onBackToHome: () -> Unit
) {
    if (level == null) {
        LevelNotFoundDialog(
            onBackToHome = onBackToHome
        )
        return
    }

    val content = level.introContent

    NightScreenContainer(
        background = NightBackgroundType.PurplePattern,
        useScreenPadding = false,
        scrollable = false,
        avoidKeyboard = false
    ) { dimensions ->
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val availableWidth =
                maxWidth -
                        dimensions.screenHorizontalPadding -
                        dimensions.screenHorizontalPadding -
                        NightSizes.backButton -
                        28.dp

            val compact = maxHeight <= 380.dp || availableWidth <= 720.dp

            val maxCardWidth = if (compact) {
                630.dp
            } else {
                690.dp
            }

            val cardWidth = if (availableWidth < maxCardWidth) {
                availableWidth
            } else {
                maxCardWidth
            }

            val cardHeight = if (compact) {
                320.dp
            } else {
                350.dp
            }

            val outerPadding = if (compact) 14.dp else 16.dp
            val gap = if (compact) 12.dp else 14.dp

            val leftColumnWidth = if (compact) {
                215.dp
            } else {
                245.dp
            }

            val rightPanelWidth =
                cardWidth - outerPadding - outerPadding - leftColumnWidth - gap

            val innerHeight = cardHeight - outerPadding - outerPadding

            BackButton(
                onBackToHome = onBackToHome,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        start = dimensions.screenHorizontalPadding,
                        top = dimensions.screenVerticalPadding
                    )
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(cardWidth)
                    .height(cardHeight),
                shape = RoundedCornerShape(20.dp),
                color = IntroOuterPurple
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(outerPadding),
                    horizontalArrangement = Arrangement.spacedBy(gap),
                    verticalAlignment = Alignment.Top
                ) {
                    LeftEnemyPanel(
                        levelTitle = "Nivel ${level.id + 1}",
                        content = content,
                        compact = compact,
                        width = leftColumnWidth,
                        height = innerHeight
                    )

                    RightInfoPanel(
                        content = content,
                        compact = compact,
                        width = rightPanelWidth,
                        height = innerHeight,
                        onStartLevel = onStartLevel
                    )
                }
            }
        }
    }
}

@Composable
private fun BackButton(
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.boton_volver),
        contentDescription = "Volver",
        modifier = modifier
            .size(NightSizes.backButton)
            .clickable {
                onBackToHome()
            },
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun LeftEnemyPanel(
    levelTitle: String,
    content: LevelIntroContent,
    compact: Boolean,
    width: Dp,
    height: Dp
) {
    val headerHeight = if (compact) 42.dp else 46.dp
    val imageSize = if (compact) 160.dp else 178.dp
    val enemyNameSize = if (compact) 17.sp else 18.sp
    val enemyNameLineHeight = if (compact) 19.sp else 20.sp

    Column(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(
                color = IntroLeftPurple,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LevelHeader(
            title = levelTitle,
            width = width - 24.dp,
            height = headerHeight
        )

        Spacer(modifier = Modifier.height(if (compact) 16.dp else 18.dp))

        EnemyImageCard(
            imageRes = content.enemyImageRes,
            enemyName = content.enemyTitle,
            size = imageSize
        )

        Spacer(modifier = Modifier.height(if (compact) 16.dp else 18.dp))

        Text(
            text = content.enemyTitle.uppercase(),
            textAlign = TextAlign.Center,
            maxLines = 2,
            fontFamily = LilitaOne,
            fontSize = enemyNameSize,
            lineHeight = enemyNameLineHeight,
            color = IntroHeaderShadow
        )
    }
}

@Composable
private fun LevelHeader(
    title: String,
    width: Dp,
    height: Dp
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height + 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            modifier = Modifier
                .width(width)
                .height(height)
                .offset(y = 7.dp),
            shape = RoundedCornerShape(6.dp),
            color = IntroHeaderShadow
        ) {}

        Surface(
            modifier = Modifier
                .width(width)
                .height(height),
            shape = RoundedCornerShape(6.dp),
            color = IntroHeaderPurple
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontFamily = LilitaOne,
                    fontSize = 23.sp,
                    color = SmokeWhite,
                    style = MaterialTheme.typography.titleLarge.copy(
                        shadow = Shadow(
                            color = NightSurface.copy(alpha = 0.45f),
                            offset = Offset(1.8f, 1.8f),
                            blurRadius = 2f
                        )
                    )
                )
            }
        }
    }
}

@Composable
private fun EnemyImageCard(
    imageRes: Int,
    enemyName: String,
    size: Dp
) {
    Surface(
        modifier = Modifier.size(size),
        shape = RoundedCornerShape(10.dp),
        color = IntroEnemyBg
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = enemyName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun RightInfoPanel(
    content: LevelIntroContent,
    compact: Boolean,
    width: Dp,
    height: Dp,
    onStartLevel: () -> Unit
) {
    val warningHeight = if (compact) 54.dp else 58.dp
    val buttonHeight = if (compact) 34.dp else 36.dp
    val buttonWidth = if (compact) 142.dp else 154.dp
    val panelPadding = if (compact) 14.dp else 16.dp

    Surface(
        modifier = Modifier
            .width(width)
            .height(height),
        shape = RoundedCornerShape(14.dp),
        color = IntroRightPurple
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(panelPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WarningCard(
                message = content.alertMessage,
                compact = compact,
                modifier = Modifier
                    .width(width - panelPadding - panelPadding)
                    .height(warningHeight)
            )

            Spacer(modifier = Modifier.height(if (compact) 14.dp else 16.dp))

            WhiteInfoCard(
                modifier = Modifier
                    .width(width - panelPadding - panelPadding)
                    .weight(1f)
            ) {
                DetailLine(
                    title = "Dificultad:",
                    content = content.difficulty,
                    highlight = true,
                    compact = compact
                )

                Spacer(modifier = Modifier.height(if (compact) 8.dp else 9.dp))

                DetailLine(
                    title = "Comportamiento:",
                    content = content.behavior,
                    compact = compact
                )

                Spacer(modifier = Modifier.height(if (compact) 8.dp else 9.dp))

                DetailLine(
                    title = "Consejo:",
                    content = content.tip,
                    compact = compact
                )
            }

            Spacer(modifier = Modifier.height(if (compact) 14.dp else 16.dp))

            Surface(
                modifier = Modifier
                    .width(buttonWidth)
                    .height(buttonHeight)
                    .clickable {
                        onStartLevel()
                    },
                shape = NightShapes.pill,
                color = CheeseYellow
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "COMENZAR",
                        fontFamily = LilitaOne,
                        fontSize = 13.sp,
                        letterSpacing = 1.5.sp,
                        color = IntroTextDark,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun WarningCard(
    message: String,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.border(
            width = 2.5.dp,
            color = PizzaRed,
            shape = RoundedCornerShape(8.dp)
        ),
        shape = RoundedCornerShape(8.dp),
        color = IntroWarningBg
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(IntroWarningBg)
                .padding(
                    horizontal = if (compact) 10.dp else 12.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = PizzaRed,
                modifier = Modifier.size(if (compact) 28.dp else 30.dp)
            )

            Spacer(modifier = Modifier.width(if (compact) 8.dp else 10.dp))

            Text(
                text = message,
                color = PizzaRed,
                fontSize = if (compact) 10.sp else 11.sp,
                lineHeight = if (compact) 12.sp else 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 3
            )
        }
    }
}

@Composable
private fun WhiteInfoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.border(
            width = 2.dp,
            color = IntroDataBorder,
            shape = RoundedCornerShape(10.dp)
        ),
        shape = RoundedCornerShape(10.dp),
        color = IntroInfoWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),
            content = content
        )
    }
}

@Composable
private fun DetailLine(
    title: String,
    content: String,
    compact: Boolean,
    highlight: Boolean = false
) {
    val bodySize = if (compact) 11.sp else 12.sp
    val bodyLineHeight = if (compact) 16.sp else 17.sp

    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = IntroTextPurple,
                    fontFamily = LilitaOne,
                    fontWeight = FontWeight.Normal,
                    fontSize = bodySize
                )
            ) {
                append(title)
                append(" ")
            }

            withStyle(
                style = SpanStyle(
                    color = if (highlight) PizzaRed else IntroTextPurple,
                    fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
                    fontSize = bodySize
                )
            ) {
                append(content)
            }
        },
        lineHeight = bodyLineHeight,
        maxLines = if (highlight) 1 else 3,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun LevelNotFoundDialog(
    onBackToHome: () -> Unit
) {
    NightMessageDialog(
        title = "Nivel no encontrado",
        message = "No se pudo cargar la información de esta noche.",
        confirmText = "Volver",
        dismissText = null,
        icon = Icons.Default.Warning,
        iconColor = CheeseYellow,
        onConfirm = onBackToHome,
        onDismiss = onBackToHome
    )
}