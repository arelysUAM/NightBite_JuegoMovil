package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.FieldBackground
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed

@Composable
fun NightTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String? = null,
    trailingIcon: ImageVector? = null,
    trailingIconDescription: String? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            placeholder = {
                Text(
                    text = label,
                    fontSize = 11.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(19.dp)
                )
            },
            trailingIcon = {
                if (trailingIcon != null && onTrailingIconClick != null) {
                    IconButton(
                        onClick = onTrailingIconClick
                    ) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = trailingIconDescription,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            },
            singleLine = true,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(26.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = FieldBackground,
                unfocusedContainerColor = FieldBackground,
                disabledContainerColor = FieldBackground,
                errorContainerColor = FieldBackground,

                focusedIndicatorColor = if (isError) PizzaRed else CheeseYellow,
                unfocusedIndicatorColor = if (isError) PizzaRed else CheeseYellow,
                errorIndicatorColor = PizzaRed,

                focusedTextColor = DarkText,
                unfocusedTextColor = DarkText,
                errorTextColor = DarkText,

                focusedLeadingIconColor = NightSurface,
                unfocusedLeadingIconColor = LavenderGray,
                errorLeadingIconColor = PizzaRed,

                focusedTrailingIconColor = NightSurface,
                unfocusedTrailingIconColor = LavenderGray,
                errorTrailingIconColor = PizzaRed,

                focusedPlaceholderColor = LavenderGray,
                unfocusedPlaceholderColor = LavenderGray,
                errorPlaceholderColor = LavenderGray,

                cursorColor = CheeseYellow,
                errorCursorColor = PizzaRed
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        )

        if (isError && !errorMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = errorMessage,
                color = PizzaRed,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}