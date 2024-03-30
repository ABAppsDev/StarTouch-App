package presentation.screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import kms

@Composable
fun IconWithBackground(
    icon: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 21.kms,
    backgroundColor: Color = Color(0xFF8D7B4B),
    borderColor: Color = backgroundColor,
    radiusSize: Dp = 60.kms,
    shape: Shape = RoundedCornerShape(radiusSize),
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(width = 1.kms, color = borderColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(iconSize).padding(8.kms),
            painter = icon,
            contentDescription = contentDescription,
        )
    }
}