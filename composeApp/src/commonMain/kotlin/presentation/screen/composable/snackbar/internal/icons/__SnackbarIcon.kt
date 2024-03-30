package presentation.screen.composable.snackbar.internal.icons

import androidx.compose.ui.graphics.vector.ImageVector
import presentation.screen.composable.snackbar.internal.icons.snackbaricon.IcError
import presentation.screen.composable.snackbar.internal.icons.snackbaricon.IcInfo
import presentation.screen.composable.snackbar.internal.icons.snackbaricon.IcSuccess
import presentation.screen.composable.snackbar.internal.icons.snackbaricon.IcWarning
import kotlin.collections.List as ____KtList

public object SnackbarIcon

private var __AllIcons: ____KtList<ImageVector>? = null

public val SnackbarIcon.AllIcons: ____KtList<ImageVector>
    get() {
        if (__AllIcons != null) {
            return __AllIcons!!
        }
        __AllIcons = listOf(IcSuccess, IcInfo, IcError, IcWarning)
        return __AllIcons!!
    }
