package com.beepbeep.designSystem.ui.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.beepbeep.designSystem.ui.theme.Theme

@Composable
fun StIconButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Int = 56,
    gapBetweenIconAndContent: Int = 8,
    content: @Composable (() -> Unit)
) {
    Surface(
        modifier = modifier
            .height(height.dp)
            .border(width = 1.dp, color = Theme.colors.divider, shape = RoundedCornerShape(
                Theme.radius.medium)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        color = Color.Transparent,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(gapBetweenIconAndContent.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painter, contentDescription = "", tint = Theme.colors.contentPrimary)
            content()
        }
    }
}