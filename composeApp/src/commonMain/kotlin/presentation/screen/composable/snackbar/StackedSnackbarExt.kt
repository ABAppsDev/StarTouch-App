package presentation.screen.composable.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.beepbeep.designSystem.ui.composable.snackbar.StackedSnackbarAnimation

@Composable
inline fun rememberStackedSnackbarHostState(
    maxStack: Int = Int.MAX_VALUE,
    animation: StackedSnackbarAnimation = StackedSnackbarAnimation.Bounce,
) = run {
    val scope = rememberCoroutineScope()
    remember {
        StackedSnakbarHostState(animation = animation, maxStack = maxStack, coroutinesScope = scope)
    }
}