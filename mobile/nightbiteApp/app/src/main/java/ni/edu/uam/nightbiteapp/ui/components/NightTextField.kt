package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.FieldBackground
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface

/**
 * Campo de texto reutilizable para formularios de NightBite.
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
        placeholder = {
            Text(text = label)
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = label
            )
        },
        singleLine = true,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(22.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = FieldBackground,
            unfocusedContainerColor = FieldBackground,
            focusedIndicatorColor = CheeseYellow,
            unfocusedIndicatorColor = CheeseYellow,
            focusedTextColor = DarkText,
            unfocusedTextColor = DarkText,
            focusedLeadingIconColor = NightSurface,
            unfocusedLeadingIconColor = LavenderGray,
            focusedPlaceholderColor = LavenderGray,
            unfocusedPlaceholderColor = LavenderGray,
            cursorColor = CheeseYellow
        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp)
    )
}