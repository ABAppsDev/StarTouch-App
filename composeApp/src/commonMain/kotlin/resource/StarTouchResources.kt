package resource

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import getPlatformContext
import resource.strings.IStringResources
import util.LanguageCode
import util.LocalizationManager

private val localStringResources = staticCompositionLocalOf<IStringResources> {
    error("CompositionLocal IStringResources not present")
}

@Composable
fun StarTouchTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    languageCode: LanguageCode,
    content: @Composable () -> Unit,
) {
    val context = getPlatformContext()
    CompositionLocalProvider(
        localStringResources provides LocalizationManager.getStringResources(languageCode),
        LocalLayoutDirection provides LocalizationManager.getLayoutDirection(languageCode),
    ) {
        content()
    }
}


object Resources {
    val strings: IStringResources
        @Composable
        @ReadOnlyComposable
        get() = localStringResources.current
}