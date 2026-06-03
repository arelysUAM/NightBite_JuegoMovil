package ni.edu.uam.nightbiteapp.ui.screens

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.ui.components.NightFloatingCloseButton
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
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
    var showUnderAgeDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val years = remember {
        (currentYear downTo 1900).toList()
    }

    val itemHeight = 42.dp
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
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .padding(horizontal = 32.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Box {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NightSurface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .width(360.dp)
                        .padding(horizontal = 28.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "VERIFICAR EDAD",
                        color = SmokeWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Desliza hasta tu año de nacimiento.",
                        color = LavenderGray,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "Debes tener 13 años o más.",
                        color = LavenderGray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

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

                    Spacer(modifier = Modifier.height(16.dp))

                    NightPrimaryButton(
                        text = "CONTINUAR",
                        icon = Icons.Default.CheckCircle,
                        onClick = {
                            val age = currentYear - selectedYear

                            if (age >= 13) {
                                onAgeApproved(age)
                            } else {
                                showUnderAgeDialog = true
                            }
                        },
                        modifier = Modifier.width(280.dp)
                    )
                }
            }

            NightFloatingCloseButton(
                onClick = onBackToLogin,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 18.dp, y = (-18).dp)
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
    listState: androidx.compose.foundation.lazy.LazyListState,
    itemHeight: Dp,
    onYearClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.width(220.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            color = CheeseYellow.copy(alpha = 0.8f)
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
                        .width(220.dp)
                        .clickable {
                            onYearClick(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = year.toString(),
                        color = if (isSelected) CheeseYellow else LavenderGray,
                        fontSize = if (isSelected) 26.sp else 17.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(itemHeight))
            }
        }

        HorizontalDivider(
            color = CheeseYellow.copy(alpha = 0.8f)
        )
    }
}