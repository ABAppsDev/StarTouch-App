import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.system.exitProcess

actual class PlatformContext

@Composable
actual fun getPlatformContext(): PlatformContext {
    return PlatformContext()
}

@Stable
actual val Int.kms: Dp
    @Composable
    get() = (this * LocalDensity.current.density).dp

@Stable
actual val Double.kms: Dp
    @Composable
    get() = (this * LocalDensity.current.density).dp

@Stable
actual val Float.kms: Dp
    @Composable
    get() = (this * LocalDensity.current.density).dp

@Composable
actual fun exitApplication() {
    exitProcess(0)
}