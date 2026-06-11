package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Botón principal reutilizable para acciones importantes.
 *
 * Se usa para acciones positivas o principales como:
 * - Ingresar
 * - Registrarse
 * - Continuar
 * - Aplicar cambios
 */
@Composable
fun NightPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = NightShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = CheeseYellow,
            contentColor = DarkText,
            disabledContainerColor = CheeseYellow.copy(alpha = 0.45f),
            disabledContentColor = DarkText.copy(alpha = 0.55f)
        ),
        contentPadding = PaddingValues(
            horizontal = 14.dp,
            vertical = 0.dp
        ),
        modifier = modifier.height(NightSizes.primaryButtonHeight)
    ) {
        NightButtonContent(
            text = text,
            icon = icon,
            iconSize = 16.dp,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black
        )
    }
}

/**
 * Botón secundario reutilizable para acciones alternativas.
 *
 * Se usa para acciones como:
 * - Cancelar
 * - Volver
 * - Continuar editando
 */
@Composable
fun NightSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = NightShapes.button,
        border = BorderStroke(1.5.dp, SmokeWhite),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = SmokeWhite,
            disabledContentColor = SmokeWhite.copy(alpha = 0.45f)
        ),
        modifier = modifier.height(NightSizes.secondaryButtonHeight)
    ) {
        NightButtonContent(
            text = text,
            icon = icon,
            iconSize = 18.dp,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Botón para acciones destructivas o delicadas.
 *
 * Se usará para:
 * - Eliminar cuenta
 * - Confirmar salida
 * - Reiniciar progreso
 */
@Composable
fun NightDangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = NightShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = ni.edu.uam.nightbiteapp.ui.theme.PizzaRed,
            contentColor = SmokeWhite,
            disabledContainerColor = ni.edu.uam.nightbiteapp.ui.theme.PizzaRed.copy(alpha = 0.45f),
            disabledContentColor = SmokeWhite.copy(alpha = 0.55f)
        ),
        contentPadding = PaddingValues(
            horizontal = 14.dp,
            vertical = 0.dp
        ),
        modifier = modifier.height(NightSizes.primaryButtonHeight)
    ) {
        NightButtonContent(
            text = text,
            icon = icon,
            iconSize = 16.dp,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun NightButtonContent(
    text: String,
    icon: ImageVector?,
    iconSize: androidx.compose.ui.unit.Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    fontWeight: FontWeight
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(iconSize)
            )
        }

        Text(
            text = if (icon != null) " $text" else text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            letterSpacing = 0.8.sp
        )
    }
}