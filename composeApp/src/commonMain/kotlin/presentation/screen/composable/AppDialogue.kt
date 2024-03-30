package presentation.screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kms

@Composable
fun AppDialogue(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF8D7B4B).copy(alpha = 0.7f),
    shape: Shape = RoundedCornerShape(16.kms),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current.density
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier.fillMaxWidth(density / 1.5f)
                .heightIn(min = 200.dp)
                .background(containerColor, shape),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }
}