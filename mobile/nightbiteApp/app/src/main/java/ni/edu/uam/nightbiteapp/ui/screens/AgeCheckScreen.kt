package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.components.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.components.NightSecondaryButton
import ni.edu.uam.nightbiteapp.ui.components.NightTextField
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import java.time.LocalDate

/**
 * Pantalla para verificar la edad antes de permitir crear una cuenta.
 *
 * El usuario debe tener 13 años o más para continuar al registro.
 */
@Composable
fun AgeCheckScreen(
    onAgeApproved: (Int) -> Unit,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    var birthYear by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = NightSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(380.dp)
                    .padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Cake,
                    contentDescription = "Verificación de edad",
                    tint = CheeseYellow
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "VERIFICAR EDAD",
                    color = SmokeWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "Debes tener 13 años o más para crear una cuenta.",
                    color = LavenderGray,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                NightTextField(
                    value = birthYear,
                    onValueChange = { value ->
                        birthYear = value.filter { it.isDigit() }.take(4)
                    },
                    label = "Año de nacimiento",
                    icon = Icons.Default.Cake,
                    modifier = Modifier.width(300.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                NightPrimaryButton(
                    text = "CONTINUAR",
                    icon = Icons.Default.CheckCircle,
                    onClick = {
                        val year = birthYear.toIntOrNull()
                        val currentYear = LocalDate.now().year

                        if (year == null || year < 1900 || year > currentYear) {
                            Toast.makeText(
                                context,
                                "Ingresa un año de nacimiento válido.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@NightPrimaryButton
                        }

                        val age = currentYear - year

                        if (age >= 13) {
                            onAgeApproved(age)
                        } else {
                            Toast.makeText(
                                context,
                                "No puedes crear una cuenta si eres menor de 13 años.",
                                Toast.LENGTH_LONG
                            ).show()

                            onBackToLogin()
                        }
                    },
                    modifier = Modifier.width(300.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                NightSecondaryButton(
                    text = "VOLVER",
                    icon = Icons.Default.ArrowBack,
                    onClick = onBackToLogin,
                    modifier = Modifier.width(300.dp)
                )
            }
        }
    }
}