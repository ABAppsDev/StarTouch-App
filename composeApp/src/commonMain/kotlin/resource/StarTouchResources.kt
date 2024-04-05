package resource

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import com.beepbeep.designSystem.ui.theme.StTheme
import resource.strings.IStringResources
import util.LanguageCode
import util.LocalizationManager
import util.setInsetsController

private val localStringResources = staticCompositionLocalOf<IStringResources> {
    error("CompositionLocal IStringResources not present")
}

@Composable
fun StarTouchTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    languageCode: LanguageCode,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        localStringResources provides LocalizationManager.getStringResources(languageCode),
        LocalLayoutDirection provides LocalizationManager.getLayoutDirection(languageCode),
    ) {
        StTheme(useDarkTheme = useDarkTheme) {
            setInsetsController(useDarkTheme)
            content()
        }
    }
}


object Resources {
    val strings: IStringResources
        @Composable
        @ReadOnlyComposable
        get() = localStringResources.current
}