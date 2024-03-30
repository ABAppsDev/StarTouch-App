import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.abapps.project.getActivity

actual class PlatformContext(val androidContext: Context)

@Composable
actual fun getPlatformContext(): PlatformContext {
    return PlatformContext(LocalContext.current)
}

@Stable
actual val Int.kms: Dp get() = this.dp

@Stable
actual val Double.kms: Dp get() = this.dp

@Stable
actual val Float.kms: Dp get() = this.dp

@Composable
actual fun exitApplication() {
    val activity = getPlatformContext().androidContext.getActivity()
    activity?.finish()
}