package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.design.getNightWindowSize
import ni.edu.uam.nightbiteapp.ui.design.nightDimensionsFor
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.LoginTabCyan
import ni.edu.uam.nightbiteapp.ui.theme.NightBackground
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import java.util.Calendar

/**
 * Pantalla para verificar la edad antes de permitir crear una cuenta.
 *
 * El usuario selecciona su año de nacimiento desplazando una lista
 * desde el año actual hasta 1900.
 */
@Composable
fun AgeCheckScreen(
    onAgeApproved: (Int) -> Unit,
    onBackToLogin: () -> Unit
) {
    var showUnderAgeDialog by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val years = remember(currentYear) {
        (currentYear downTo MIN_YEAR).toList()
    }

    val itemHeight = 36.dp

    val itemHeightPx = with(LocalDensity.current) {
        itemHeight.toPx()
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 0
    )

    val selectedIndex by remember {
        derivedStateOf {
            val offset = listState.firstVisibleItemScrollOffset
            val baseIndex = listState.firstVisibleItemIndex

            val index = if (offset > itemHeightPx / 2) {
                baseIndex + 1
            } else {
                baseIndex
            }

            index.coerceIn(0, years.lastIndex)
        }
    }

    val selectedYear = years[selectedIndex]

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(NightBackground)
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        val windowSize = getNightWindowSize(maxWidth)
        val dimensions = nightDimensionsFor(windowSize)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = dimensions.screenHorizontalPadding,
                    vertical = dimensions.screenVerticalPadding
                ),
            contentAlignment = Alignment.Center
        ) {
            AgeCheckCard(
                years = years,
                selectedYear = selectedYear,
                listState = listState,
                itemHeight = itemHeight,
                cardSize = dimensions.ageCheckCardSize,
                pickerWidth = dimensions.agePickerWidth,
                iconButtonSize = dimensions.iconButtonSize,
                onYearClick = { index ->
                    coroutineScope.launch {
                        listState.animateScrollToItem(index)
                    }
                },
                onContinueClick = {
                    val age = currentYear - selectedYear

                    if (age >= MIN_ALLOWED_AGE) {
                        onAgeApproved(age)
                    } else {
                        showUnderAgeDialog = true
                    }
                },
                onBackToLogin = onBackToLogin
            )

            if (showUnderAgeDialog) {
                UnderAgeDialog(
                    onConfirm = {
                        showUnderAgeDialog = false
                        onBackToLogin()
                    }
                )
            }
        }
    }
}

@Composable
private fun AgeCheckCard(
    years: List<Int>,
    selectedYear: Int,
    listState: LazyListState,
    itemHeight: Dp,
    cardSize: Dp,
    pickerWidth: Dp,
    iconButtonSize: Dp,
    onYearClick: (Int) -> Unit,
    onContinueClick: () -> Unit,
    onBackToLogin: () -> Unit
) {
    Box {
        Card(
            modifier = Modifier
                .size(cardSize)
                .shadow(
                    elevation = 8.dp,
                    shape = NightShapes.dialog
                ),
            shape = NightShapes.dialog,
            colors = CardDefaults.cardColors(
                containerColor = LoginTabCyan,
                contentColor = DarkText
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = NightSpacing.extraLarge,
                        vertical = NightSpacing.extraLarge
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                AgeCheckTitleBlock()

                YearPicker(
                    years = years,
                    selectedYear = selectedYear,
                    listState = listState,
                    itemHeight = itemHeight,
                    pickerWidth = pickerWidth,
                    onYearClick = onYearClick
                )

                NightPrimaryButton(
                    text = "CONTINUAR",
                    onClick = onContinueClick,
                    modifier = Modifier.width(130.dp)
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.boton_cancelar),
            contentDescription = "Cerrar",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(
                    x = 18.dp,
                    y = (-18).dp
                )
                .size(iconButtonSize)
                .clickable {
                    onBackToLogin()
                },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun AgeCheckTitleBlock() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "VERIFICAR EDAD",
            color = DarkText,
            fontSize = 25.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(NightSpacing.extraSmall))

        Text(
            text = "Selecciona tu año de nacimiento",
            color = DarkText.copy(alpha = 0.72f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun YearPicker(
    years: List<Int>,
    selectedYear: Int,
    listState: LazyListState,
    itemHeight: Dp,
    pickerWidth: Dp,
    onYearClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .width(pickerWidth)
            .background(
                color = Color(0xFF37A9AD),
                shape = NightShapes.smallCard
            )
            .padding(
                horizontal = NightSpacing.large,
                vertical = NightSpacing.large
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        YearPickerDivider()

        LazyColumn(
            state = listState,
            modifier = Modifier.height(itemHeight * 3),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(itemHeight))
            }

            items(years.size) { index ->
                YearItem(
                    year = years[index],
                    isSelected = years[index] == selectedYear,
                    itemHeight = itemHeight,
                    onClick = {
                        onYearClick(index)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(itemHeight))
            }
        }

        YearPickerDivider()
    }
}

@Composable
private fun YearItem(
    year: Int,
    isSelected: Boolean,
    itemHeight: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(itemHeight)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = year.toString(),
            color = if (isSelected) {
                SmokeWhite
            } else {
                SmokeWhite.copy(alpha = 0.38f)
            },
            fontSize = if (isSelected) {
                35.sp
            } else {
                23.sp
            },
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun YearPickerDivider() {
    HorizontalDivider(
        color = SmokeWhite,
        thickness = 2.dp
    )
}

@Composable
private fun UnderAgeDialog(
    onConfirm: () -> Unit
) {
    NightMessageDialog(
        title = "Edad no permitida",
        message = "No puedes crear una cuenta si eres menor de 13 años.",
        confirmText = "ENTENDIDO",
        icon = Icons.Default.Error,
        iconColor = PizzaRed,
        onConfirm = onConfirm
    )
}

private const val MIN_YEAR = 1900
private const val MIN_ALLOWED_AGE = 13