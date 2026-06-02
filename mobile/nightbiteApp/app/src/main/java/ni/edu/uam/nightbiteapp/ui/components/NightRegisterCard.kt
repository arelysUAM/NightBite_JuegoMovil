package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Tarjeta visual horizontal para el registro de nuevos jugadores.
 *
 * Aprovecha la orientación horizontal de la app y distribuye mejor
 * el espacio para identidad visual, formulario y botones.
 */
@Composable
fun NightRegisterCard(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = NightSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .widthIn(min = 620.dp, max = 760.dp)
                .height(310.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RegisterInfoPanel(
                modifier = Modifier
                    .weight(0.9f)
                    .height(310.dp)
            )

            RegisterFormPanel(
                username = username,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                onUsernameChange = onUsernameChange,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onConfirmPasswordChange = onConfirmPasswordChange,
                onRegisterClick = onRegisterClick,
                onBackToLoginClick = onBackToLoginClick,
                modifier = Modifier
                    .weight(1.1f)
                    .height(310.dp)
            )
        }
    }
}

@Composable
private fun RegisterInfoPanel(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(NightSurface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CREAR CUENTA",
            color = SmokeWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            text = "Registra tu repartidor nocturno",
            color = LavenderGray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(82.dp)
                .clip(CircleShape)
                .background(CheeseYellow.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DeliveryDining,
                contentDescription = "Driver",
                tint = CheeseYellow,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "NIGHTBITE",
            color = CheeseYellow,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            text = "Delivery nocturno",
            color = LavenderGray,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun RegisterFormPanel(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(NightSurface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NightTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = "Nombre de jugador",
            icon = Icons.Default.Person,
            modifier = Modifier.width(320.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        NightTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            icon = Icons.Default.Email,
            modifier = Modifier.width(320.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        NightTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            icon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(320.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        NightTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirmar contraseña",
            icon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(320.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NightSecondaryButton(
                text = "VOLVER",
                onClick = onBackToLoginClick,
                icon = Icons.Default.ArrowBack,
                modifier = Modifier.width(140.dp)
            )

            NightPrimaryButton(
                text = "REGISTRARSE",
                onClick = onRegisterClick,
                icon = Icons.Default.PersonAdd,
                modifier = Modifier.width(170.dp)
            )
        }
    }
}