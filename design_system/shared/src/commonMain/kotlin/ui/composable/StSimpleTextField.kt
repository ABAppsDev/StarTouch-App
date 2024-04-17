package com.beepbeep.designSystem.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.composable.modifier.noRippleEffect
import com.beepbeep.designSystem.ui.theme.Theme

@Composable
fun StSimpleTextField(
    text: String,
    hint: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit = {},
    hintColor: Color = Theme.colors.contentTertiary,
    modifier: Modifier = Modifier,
    trailingPainter: Painter? = null,
    leadingPainter: Painter? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    radius: Dp = Theme.radius.medium,
    errorMessage: String = "",
    isError: Boolean = errorMessage.isNotEmpty(),
    onTrailingIconClick: () -> Unit = {},
    isSingleLine: Boolean = true,
    trailingIconEnabled: Boolean = onTrailingIconClick != {},
    outlinedTextFieldDefaults: TextFieldColors = OutlinedTextFieldColorDefaults()
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp, max = 160.dp),
            value = text,
            placeholder = {
                Text(
                    hint,
                    style = Theme.typography.body,
                    color = hintColor,
                )
            },
            onValueChange = onValueChange,
            shape = RoundedCornerShape(radius),
            textStyle = Theme.typography.body.copy(color = Theme.colors.contentPrimary),
            singleLine = isSingleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = isError,
            trailingIcon = {
                trailingPainter?.let {
                    IconButton(
                        onClick = onTrailingIconClick,
                        enabled = trailingIconEnabled,
                    ) {
                        Icon(
                            painter = trailingPainter,
                            contentDescription = "trailing icon",
                            tint = Theme.colors.contentTertiary
                        )
                    }
                }
            },
            leadingIcon = if (leadingPainter != null) {
                {
                    Icon(
                        painter = leadingPainter,
                        contentDescription = "leading icon",
                        tint = Theme.colors.contentSecondary,
                        modifier = Modifier.noRippleEffect(onClick = onClick)
                    )
                }
            } else null,
            colors = outlinedTextFieldDefaults,
        )
        AnimatedVisibility(isError) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 8.dp),
                style = Theme.typography.caption,
                color = Theme.colors.primary
            )
        }
    }

}

@Composable
fun OutlinedTextFieldColorDefaults() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Theme.colors.surface,
    unfocusedContainerColor = Theme.colors.surface,
    cursorColor = Theme.colors.contentTertiary,
    errorCursorColor = Theme.colors.primary,
    focusedBorderColor = Theme.colors.contentTertiary.copy(alpha = 0.2f),
    unfocusedBorderColor = Theme.colors.contentBorder.copy(alpha = 0.1f),
    errorBorderColor = Theme.colors.primary.copy(alpha = 0.5f),
)

