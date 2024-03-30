package presentation.screen.composable.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Dp.toPx(): Float = with(LocalDensity.current) { toPx() }

//@Composable
//fun Dp.toResponsive(): Dp = (this.toPx() * LocalDensity.current.density).dp