package com.beepbeep.designSystem.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.theme.Theme

@Composable
fun StSnackBar(
    icon: Painter,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    iconTint: Color = Theme.colors.primary,
    iconBackgroundColor: Color = Theme.colors.hover,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
    ) {
        Column(
            modifier = modifier.fillMaxWidth().background(Theme.colors.background)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(
                        width = 1.dp,
                        color = Theme.colors.hover,
                        shape = RoundedCornerShape(size = Theme.radius.medium)
                    )
                    .background(
                        color = Theme.colors.background,
                        shape = RoundedCornerShape(size = Theme.radius.medium)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    modifier = Modifier.background(
                        color = iconBackgroundColor,
                        shape = RoundedCornerShape(Theme.radius.medium)
                    ).padding(8.dp),
                    painter = icon,
                    contentDescription = null,
                    tint = iconTint
                )
                content()
            }
        }
    }
}