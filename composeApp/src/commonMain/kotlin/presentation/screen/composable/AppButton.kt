package presentation.screen.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import presentation.screen.composable.modifier.bounceClick

@Composable
fun AppButton(
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textPadding: PaddingValues = PaddingValues(8.dp),
    shape: Shape = RoundedCornerShape(16.dp),
    containerColor: Color = Color.White,
    contentColor: Color = Color.Black,
    disabledColor: Color = Color(0xFFBFBFBF),
    disabledTextColor: Color = Color.White,
    border: BorderStroke = BorderStroke(1.dp, if (enabled) containerColor else disabledColor),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    val buttonColor by animateColorAsState(
        if (enabled) containerColor else disabledColor,
    )
    Surface(
        modifier = modifier.height(56.dp).semantics { role = Role.Button }
            .bounceClick(
                enabled = enabled,
                onClick = {
                    if (!isLoading && enabled) onClick()
                }
            ),
        shape = shape,
        color = buttonColor,
        contentColor = contentColor,
        border = border,
    ) {
        Row(
            Modifier.defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = ButtonDefaults.MinHeight
            ),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedContent(targetState = isLoading) {
                if (isLoading) AppThreeDotLoadingIndicator()
                else Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (enabled) contentColor else disabledTextColor,
                    modifier = Modifier.padding(textPadding),
                )
            }
        }
    }
}