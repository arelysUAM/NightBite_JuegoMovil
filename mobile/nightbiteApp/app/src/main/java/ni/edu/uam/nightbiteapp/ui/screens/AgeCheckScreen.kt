package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.LoginTabCyan
import ni.edu.uam.nightbiteapp.ui.theme.NightBackground
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import java.util.Calendar
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextAlign

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
    var showUnderAgeDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val years = remember {
        (currentYear downTo 1900).toList()
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NightBackground)
            .imePadding()
            .padding(horizontal = 40.dp, vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Box {
            Card(
                modifier = Modifier
                    .width(320.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(28.dp)
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LoginTabCyan
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
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

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Selecciona tu año de nacimiento",
                        color = DarkText.copy(alpha = 0.72f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    YearPicker(
                        years = years,
                        selectedYear = selectedYear,
                        listState = listState,
                        itemHeight = itemHeight,
                        onYearClick = { index ->
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    NightPrimaryButton(
                        text = "CONTINUAR",
                        onClick = {
                            val age = currentYear - selectedYear

                            if (age >= 13) {
                                onAgeApproved(age)
                            } else {
                                showUnderAgeDialog = true
                            }
                        },
                        modifier = Modifier.width(120.dp)
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.boton_cancelar),
                contentDescription = "Cerrar",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .width(72.dp)
                    .clickable {
                        onBackToLogin()
                    }
            )
        }

        if (showUnderAgeDialog) {
            NightMessageDialog(
                title = "Edad no permitida",
                message = "No puedes crear una cuenta si eres menor de 13 años.",
                confirmText = "ENTENDIDO",
                icon = Icons.Default.Error,
                iconColor = PizzaRed,
                onConfirm = {
                    showUnderAgeDialog = false
                    onBackToLogin()
                }
            )
        }
    }
}

@Composable
private fun YearPicker(
    years: List<Int>,
    selectedYear: Int,
    listState: LazyListState,
    itemHeight: Dp,
    onYearClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .width(280.dp)
            .background(
                color = Color(0xFF37A9AD),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            color = SmokeWhite,
            thickness = 2.dp
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.height(itemHeight * 3),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(itemHeight))
            }

            items(years.size) { index ->
                val year = years[index]
                val isSelected = year == selectedYear

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .width(213.dp)
                        .clickable {
                            onYearClick(index)
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
                        fontSize = if (isSelected) 35.sp else 23.sp,
                        fontFamily = LilitaOne,
                        fontWeight = if (isSelected) {
                            FontWeight.Normal
                        } else {
                            FontWeight.Normal
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(itemHeight))
            }
        }

        HorizontalDivider(
            color = SmokeWhite,
            thickness = 2.dp
        )
    }
}