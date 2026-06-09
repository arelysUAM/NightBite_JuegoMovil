package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Botón principal reutilizable para acciones importantes.
 */
@Composable
fun NightPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CheeseYellow,
            contentColor = DarkText
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 14.dp,
            vertical = 0.dp
        ),
        modifier = modifier.height(34.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = if (icon != null) "  $text" else text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.8.sp
            )
        }
    }
}

/**
 * Botón secundario reutilizable para acciones alternativas.
 */
@Composable
fun NightSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.5.dp, SmokeWhite),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = SmokeWhite
        ),
        modifier = modifier.height(38.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = if (icon != null) "  $text" else text,
                fontWeight = FontWeight.Bold
            )
        }
    }
}