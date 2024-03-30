package presentation.screen.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun SetLayoutDirection(layoutDirection: LayoutDirection, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        content()
    }
}