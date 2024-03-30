import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp

expect class PlatformContext

@Composable
expect fun getPlatformContext(): PlatformContext

@Stable
expect val Int.kms: Dp

@Stable
expect val Double.kms: Dp

@Stable
expect val Float.kms: Dp

@Composable
expect fun exitApplication()