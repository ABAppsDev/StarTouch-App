package presentation.screen.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import presentation.base.ErrorState
import com.beepbeep.designSystem.ui.composable.animate.FadeAnimation
import com.beepbeep.designSystem.ui.composable.animate.SlideAnimation
import presentation.screen.composable.snackbar.StackedSnackbarHost
import presentation.screen.composable.snackbar.StackedSnakbarHostState
import resource.Resources

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    isLoading: Boolean = false,
    error: ErrorState? = null,
    titleError: String = Resources.strings.error,
    onClickRetry: () -> Unit = {},
//    onRefresh: () -> Unit = {},
//    isRefreshing: Boolean = false,
    onLoading: @Composable () -> Unit = { LoadingScreen() },
    onError: @Composable () -> Unit = {
        HandleErrorState(titleError, error ?: ErrorState.UnknownError(""), {}, onClickRetry)
    },
    stackedSnackbarHostState: StackedSnakbarHostState? = null,
    content: @Composable () -> Unit
) {
//    val pullRefreshState = rememberPullRefreshState(isRefreshing, { onRefresh() })

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = backgroundColor,
        snackbarHost = { stackedSnackbarHostState?.let { StackedSnackbarHost(hostState = it) } },
    ) {
        FadeAnimation(
            visible = isLoading,
        ) {
            onLoading()
        }
        FadeAnimation(
            visible = error != null,
        ) {
            onError()
        }
        FadeAnimation(
            visible = !isLoading,
        ) {
            content()
        }
    }
}