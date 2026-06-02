package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Campo de texto reutilizable para formularios de NightBite.
 *
 * Se utiliza en pantallas como Login y Registro para mantener
 * una apariencia visual consistente.
 */
@Composable
fun NightTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF4A4A68)
            )
        },
        singleLine = true,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF8F5E9),
            unfocusedContainerColor = Color(0xFFF8F5E9),
            focusedIndicatorColor = Color(0xFFFFD166),
            unfocusedIndicatorColor = Color(0xFFFFD166),
            focusedLabelColor = Color(0xFF1B1F3B),
            unfocusedLabelColor = Color(0xFF4A4A68),
            focusedTextColor = Color(0xFF1B1F3B),
            unfocusedTextColor = Color(0xFF1B1F3B),
            cursorColor = Color(0xFFFFD166)
        ),
        modifier = modifier.fillMaxWidth()
    )
}