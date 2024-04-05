package com.beepbeep.designSystem.ui.composable.snackbar.internal.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.beepbeep.designSystem.ui.composable.snackbar.internal.icons.snackbaricon.IcError
import com.beepbeep.designSystem.ui.composable.snackbar.internal.icons.snackbaricon.IcInfo
import com.beepbeep.designSystem.ui.composable.snackbar.internal.icons.snackbaricon.IcSuccess
import com.beepbeep.designSystem.ui.composable.snackbar.internal.icons.snackbaricon.IcWarning
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
