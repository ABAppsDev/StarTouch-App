package presentation.screen.order.presets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import presentation.base.ErrorState
import presentation.screen.composable.HandleErrorState
import presentation.screen.composable.ShimmerListItem
import presentation.screen.composable.animate.FadeAnimation
import presentation.screen.order.composable.ChooseItem
import presentation.screen.order.composable.ChooseItemLoading
import presentation.screen.order.items.ItemsListScreen
import presentation.util.EventHandler
import resource.Resources
import util.getScreenModel

class PresetsListScreen : Screen {
    @Composable
    override fun Content() {
        val presetScreenModel = getScreenModel<PresetsListScreenModel>()
        val state by presetScreenModel.state.collectAsState()

        EventHandler(presetScreenModel.effect) { effect, navigator ->
            when (effect) {
                is PresetsUiEffect.NavigateToItemsListScreen -> {
                    navigator.push(ItemsListScreen(effect.presetId))
                }
            }
        }

        FadeAnimation(visible = state.isLoading) {
            ShimmerListItem(columnsCount = 4) { ChooseItemLoading() }
        }
        LaunchedEffect(state.errorState) {
            if (state.errorState != null) presetScreenModel.showErrorScreen()
        }
        FadeAnimation(state.showErrorScreen) {
            HandleErrorState(
                title = state.errorMessage,
                error = state.errorState
                    ?: ErrorState.UnknownError(Resources.strings.somethingWrongHappened),
                onClick = presetScreenModel::retry
            )
        }
        FadeAnimation(!state.isLoading) {
            OnRender(state, presetScreenModel as PresetsInteractionListener)
        }
    }

    @Composable
    private fun OnRender(
        state: PresetsListState,
        listener: PresetsInteractionListener
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(state.presetItemsState) { preset ->
                    ChooseItem(preset.name) {
                        listener.onClickPreset(preset.id)
                    }
                }
            }
        }
    }
}