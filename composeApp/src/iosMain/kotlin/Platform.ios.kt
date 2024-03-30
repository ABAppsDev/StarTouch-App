import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

actual class PlatformContext(val iosController: ProvidableCompositionLocal<UIViewController>)

@Composable
actual fun getPlatformContext(): PlatformContext = PlatformContext(LocalUIViewController)

@Stable
actual val Int.kms: Dp get() = this.dp

@Stable
actual val Double.kms: Dp get() = this.dp

@Stable
actual val Float.kms: Dp get() = this.dp

@Composable
actual fun exitApplication() {
}
