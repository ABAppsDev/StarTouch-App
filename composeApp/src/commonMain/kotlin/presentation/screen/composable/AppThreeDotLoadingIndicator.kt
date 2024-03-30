package presentation.screen.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import kms
import kotlinx.coroutines.delay

@Composable
fun AppThreeDotLoadingIndicator(
    modifier: Modifier = Modifier,
    dotSize: Dp = 12.kms,
    dotPadding: Dp = 2.kms,
    dotColor: Color = Color(0xFF8D7B4B),
    animationDelay: Int = 600,
    initialAlpha: Float = 0.38f,
) {

    val circles = remember {
        mutableStateListOf(
            Animatable(initialValue = initialAlpha),
            Animatable(initialValue = initialAlpha),
            Animatable(initialValue = initialAlpha)
        )
    }

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            delay((animationDelay / circles.size).toLong() * index)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = animationDelay),
                    repeatMode = RepeatMode.Reverse
                ),
            )
        }
    }

    Row(modifier = modifier) {
        circles.forEach { animatable ->
            Box(
                modifier = Modifier
                    .padding(dotPadding)
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(dotColor.copy(alpha = animatable.value))
            )
        }
    }
}