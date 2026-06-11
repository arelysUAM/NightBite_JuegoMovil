package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.FieldBackground
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NeonGreen
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
    isSuccess: Boolean = false,
    errorMessage: String? = null,
    trailingIcon: ImageVector? = null,
    trailingIconDescription: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    reserveErrorSpace: Boolean = false,
    fieldHeight: Dp = NightSizes.textFieldHeight
) {
    val indicatorColor = when {
        isError -> PizzaRed
        isSuccess -> NeonGreen
        else -> CheeseYellow
    }

    val leadingIconColor = when {
        isError -> PizzaRed
        isSuccess -> NeonGreen
        else -> NightSurface
    }

    val trailingIconColor = when {
        isError -> PizzaRed
        isSuccess -> NeonGreen
        else -> NightSurface
    }

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
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(NightSizes.iconMedium)
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
                            modifier = Modifier.size(NightSizes.iconMedium)
                        )
                    }
                }
            },
            singleLine = true,
            visualTransformation = visualTransformation,
            shape = NightShapes.textField,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = FieldBackground,
                unfocusedContainerColor = FieldBackground,
                disabledContainerColor = FieldBackground,
                errorContainerColor = FieldBackground,

                focusedIndicatorColor = indicatorColor,
                unfocusedIndicatorColor = indicatorColor,
                errorIndicatorColor = PizzaRed,

                focusedTextColor = DarkText,
                unfocusedTextColor = DarkText,
                errorTextColor = DarkText,

                focusedLeadingIconColor = leadingIconColor,
                unfocusedLeadingIconColor = leadingIconColor,
                errorLeadingIconColor = PizzaRed,

                focusedTrailingIconColor = trailingIconColor,
                unfocusedTrailingIconColor = trailingIconColor,
                errorTrailingIconColor = PizzaRed,

                focusedPlaceholderColor = LavenderGray,
                unfocusedPlaceholderColor = LavenderGray,
                errorPlaceholderColor = LavenderGray,

                cursorColor = CheeseYellow,
                errorCursorColor = PizzaRed
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(fieldHeight)
        )

        if (reserveErrorSpace) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (isError && !errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        color = PizzaRed,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = NightSizes.iconMedium)
                    )
                }
            }
        } else {
            if (isError && !errorMessage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(NightSpacing.extraSmall))

                Text(
                    text = errorMessage,
                    color = PizzaRed,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = NightSpacing.section)
                )
            }
        }
    }
}