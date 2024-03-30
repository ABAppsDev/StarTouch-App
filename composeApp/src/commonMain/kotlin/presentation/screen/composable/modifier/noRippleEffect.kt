package presentation.screen.composable.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.noRippleEffect(
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) = clickable(
    interactionSource = interactionSource,
    indication = null
) {
    onClick()
}