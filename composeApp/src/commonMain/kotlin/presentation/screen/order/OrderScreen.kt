package presentation.screen.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.popUntil
import presentation.base.ErrorState
import presentation.screen.composable.HandleErrorState
import presentation.screen.composable.ShimmerListItem
import presentation.screen.composable.animate.FadeAnimation
import presentation.screen.home.HomeScreen
import presentation.screen.order.composable.ChooseItemLoading
import presentation.util.EventHandler
import resource.Resources
import util.getScreenModel

class OrderScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel: OrderScreenModel = getScreenModel()
        val state by screenModel.state.collectAsState()

        EventHandler(screenModel.effect) { effect, navigator ->
            when (effect) {
                is OrderUiEffect.NavigateBackToDinIn -> {
                    navigator.pop()
                }

                is OrderUiEffect.NavigateBackToHome -> {
                    navigator.popUntil<HomeScreen, Screen>()
                }
            }
        }

        FadeAnimation(visible = state.isLoading) {
            ShimmerListItem(columnsCount = 4) { ChooseItemLoading() }
        }
        LaunchedEffect(state.errorState) {
            if (state.errorState != null) screenModel.showErrorScreen()
        }
        FadeAnimation(state.showErrorScreen) {
            HandleErrorState(
                title = state.errorMessage,
                error = state.errorState
                    ?: ErrorState.UnknownError(Resources.strings.somethingWrongHappened),
                onClick = screenModel::retry
            )
        }
    }
}