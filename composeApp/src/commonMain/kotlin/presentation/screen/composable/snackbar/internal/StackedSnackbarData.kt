package presentation.screen.composable.snackbar.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import presentation.screen.composable.snackbar.internal.icons.SnackbarIcon
import presentation.screen.composable.snackbar.internal.icons.snackbaricon.IcError
import presentation.screen.composable.snackbar.internal.icons.snackbaricon.IcInfo
import presentation.screen.composable.snackbar.internal.icons.snackbaricon.IcSuccess
import presentation.screen.composable.snackbar.internal.icons.snackbaricon.IcWarning
import presentation.screen.composable.snackbar.StackedSnackbarDuration

@Stable
internal sealed class StackedSnackbarData(val showDuration: StackedSnackbarDuration) {
    data class Normal(
        val type: Type,
        val title: String,
        val description: String? = null,
        val actionTitle: String? = null,
        val action: (() -> Unit)? = null,
        val duration: StackedSnackbarDuration = StackedSnackbarDuration.Short,
    ) : StackedSnackbarData(duration)

    data class Custom(
        val content: @Composable (() -> Unit) -> Unit,
        val duration: StackedSnackbarDuration = StackedSnackbarDuration.Short,
    ) : StackedSnackbarData(duration)
}

@Stable
internal enum class Type(val icon: ImageVector, val color: Color) {
    Info(SnackbarIcon.IcInfo, SnackbarColor.Info),
    Warning(SnackbarIcon.IcWarning, SnackbarColor.Warning),
    Error(SnackbarIcon.IcError, SnackbarColor.Error),
    Success(SnackbarIcon.IcSuccess, SnackbarColor.Success),
}