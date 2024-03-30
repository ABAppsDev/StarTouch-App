package presentation.screen.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth().height(56.dp),
    textColor: Color = Color.Black,
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    shapeRadius: Shape = RoundedCornerShape(16.dp),
    singleLine: Boolean = true,
    isError: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    var showPassword by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier,
        value = text,
        readOnly = readOnly,
        enabled = enabled,
        placeholder = {
            Text(
                hint,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.87f)
            )
        },
        isError = isError,
        leadingIcon = leadingIcon,
        onValueChange = onValueChange,
        shape = shapeRadius,
        textStyle = MaterialTheme.typography.labelLarge.copy(color = textColor),
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = if (keyboardType == KeyboardType.Password) {
            { TrailingIcon(showPassword) { showPassword = !showPassword } }
        } else trailingIcon,
        visualTransformation = BpVisualTransformation(keyboardType, showPassword),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = colors.surface,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
            focusedBorderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorCursorColor = Color.Black,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            errorContainerColor = MaterialTheme.colorScheme.errorContainer
        ),
    )
}

@Composable
private fun TrailingIcon(
    showPassword: Boolean,
    togglePasswordVisibility: () -> Unit
) {
    IconButton(onClick = { togglePasswordVisibility() }) {
        Icon(
            imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
            contentDescription = if (showPassword) "Show Password" else "Hide Password",
            tint = MaterialTheme.colorScheme.tertiary
        )
    }

}

@Composable
private fun BpVisualTransformation(
    keyboardType: KeyboardType,
    showPassword: Boolean
): VisualTransformation {
    return if (showPassword || keyboardType != KeyboardType.Password && keyboardType != KeyboardType.NumberPassword) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
}