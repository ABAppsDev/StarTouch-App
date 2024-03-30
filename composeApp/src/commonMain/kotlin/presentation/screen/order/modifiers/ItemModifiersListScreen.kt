package presentation.screen.order.modifiers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.popUntil
import org.koin.core.parameter.parametersOf
import presentation.base.ErrorState
import presentation.screen.composable.HandleErrorState
import presentation.screen.composable.ShimmerListItem
import presentation.screen.composable.animate.FadeAnimation
import presentation.screen.composable.snackbar.StackedSnackbarDuration
import presentation.screen.composable.snackbar.StackedSnackbarHost
import presentation.screen.composable.snackbar.rememberStackedSnackbarHostState
import presentation.screen.order.composable.ChooseItem
import presentation.screen.order.composable.ChooseItemLoading
import presentation.screen.order.presets.PresetsListScreen
import presentation.util.EventHandler
import resource.Resources
import util.getScreenModel

class ItemModifiersListScreen(private val items: List<ItemModifierState>) : Screen {
    @Composable
    override fun Content() {
        val itemModifiersScreenModel =
            getScreenModel<ItemModifiersListScreenModel>(parameters = { parametersOf(items) })
        val state by itemModifiersScreenModel.state.collectAsState()
        val message = Resources.strings.itemAddedSuccess

        EventHandler(itemModifiersScreenModel.effect) { effect, navigator ->
            when (effect) {
                is ItemModifiersUiEffect.NavigateBackToPresetsListScreen -> navigator.popUntil<PresetsListScreen, Screen>()
            }
        }

        FadeAnimation(visible = state.isLoading) {
            ShimmerListItem(columnsCount = 4) { ChooseItemLoading() }
        }
        FadeAnimation(state.errorState != null) {
            HandleErrorState(
                title = state.errorMessage,
                error = state.errorState
                    ?: ErrorState.UnknownError(Resources.strings.somethingWrongHappened),
                onClick = itemModifiersScreenModel::retry
            )
        }
        val stackedSnackbarHostState = rememberStackedSnackbarHostState(maxStack = 1)

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { StackedSnackbarHost(hostState = stackedSnackbarHostState) },
        ) {
            LaunchedEffect(state.isLoading) {
                if (state.isSuccess)
                    stackedSnackbarHostState.showSuccessSnackbar(
                        title = message,
                        duration = StackedSnackbarDuration.Short
                    )
            }
            FadeAnimation(!state.isLoading) {
                OnRender(state, itemModifiersScreenModel as ItemModifiersInteractionListener)
            }
        }
    }
}

@Composable
private fun OnRender(
    state: ItemModifiersListState,
    listener: ItemModifiersInteractionListener
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(state.itemModifiersState) { item ->
                ChooseItem(item.name) {
                    listener.onClickItemModifier(item.id)
                }
            }
        }
    }
}