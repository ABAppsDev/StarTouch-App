package com.beepbeep.designSystem.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.beepbeep.designSystem.ui.theme.Theme

@Composable
fun StDialogue(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .shadow(elevation = 5.dp)
                .background(Theme.colors.surface, RoundedCornerShape(Theme.radius.medium))
                .padding(24.dp),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
//            Text(
//                text = "Your info",
//                style = Theme.typography.headlineLarge,
//                color = Theme.colors.contentPrimary,
//            )
//
//            StTextField(
//                modifier = Modifier.padding(top = 40.dp),
//                label = "User name",
//                onValueChange = {},
//                text = "",
//                errorMessage = "",
//                isError = false
//            )
//
//            StTextField(
//                modifier = Modifier.padding(top = 24.dp),
//                label = "Password",
//                onValueChange = {},
//                text = "",
//                errorMessage = "",
//                isError = false
//            )
//            Row(
//                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//
//                StOutlinedButton(
//                    "Cancel",
//                    onClick = {},
//                    modifier = Modifier.width(120.dp)
//                )
//                StButton(
//                    title = "Ok",
//                    onClick = {},
//                    modifier = Modifier.fillMaxWidth(),
//                    enabled = true
//                )
//            }
        }
    }
}