package presentation.screen.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import presentation.screen.home.HomeScreen
import presentation.screen.order.composable.ChooseItem
import presentation.screen.order.composable.ChooseItemLoading
import presentation.util.EventHandler
import resource.Resources
import util.getScreenModel

class OrderScreen(private val checkId: Long) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val screenModel: OrderScreenModel = getScreenModel(parameters = { parametersOf(checkId) })
        val state by screenModel.state.collectAsState()
        val pullRefreshState = rememberPullRefreshState(state.isRefresh, { screenModel.retry() })

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

        FadeAnimation(state.presetItemsState.isNotEmpty() && !state.isPresetVisible && state.itemsState.isEmpty()) {
            PresetsList(
                state.presetItemsState,
                onClickPreset = screenModel::onClickPreset,
                modifier = Modifier.pullRefresh(pullRefreshState),
                pullRefreshState = pullRefreshState,
                isRefresh = state.isRefresh
            )
        }
        Column {
            FadeAnimation(state.isPresetVisible && state.presetItemsState.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(4),
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(state.presetItemsState) { preset ->
                            ChooseItem(preset.name, shape = CircleShape) {
                                screenModel.onClickPreset(preset.id)
                            }
                        }
                    }
                }
            }
        }
        FadeAnimation(state.itemsState.isNotEmpty()) {
            ItemsList(
                state.itemsState,
                onClickItem = screenModel::onClickItem,
                modifier = Modifier.pullRefresh(pullRefreshState),
                pullRefreshState = pullRefreshState,
                isRefresh = state.isRefresh
            )
        }
        FadeAnimation(state.itemChildrenState.isNotEmpty()) {
            ItemChildrenList(
                state.itemChildrenState,
                onClickItemChildren = screenModel::onClickItemChild,
                modifier = Modifier.pullRefresh(pullRefreshState),
                pullRefreshState = pullRefreshState,
                isRefresh = state.isRefresh
            )
        }
        FadeAnimation(state.itemModifiersState.isNotEmpty()) {
            ItemModifiersList(
                state.itemModifiersState,
                onClickItemModifier = screenModel::onClickItemModifier,
                modifier = Modifier.pullRefresh(pullRefreshState),
                pullRefreshState = pullRefreshState,
                isRefresh = state.isRefresh
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PresetsList(
    presets: List<PresetItemState>,
    modifier: Modifier = Modifier,
    onClickPreset: (Int) -> Unit,
    pullRefreshState: PullRefreshState,
    isRefresh: Boolean,
) {
    Box(modifier = modifier.fillMaxSize()) {
        PullRefreshIndicator(
            isRefresh,
            pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color(0xFF8D7B4B)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(presets) { preset ->
                ChooseItem(preset.name) {
                    onClickPreset(preset.id)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ItemsList(
    items: List<ItemState>,
    modifier: Modifier = Modifier,
    onClickItem: (Int) -> Unit,
    pullRefreshState: PullRefreshState,
    isRefresh: Boolean,
) {
    Box(modifier = modifier.fillMaxSize()) {
        PullRefreshIndicator(
            isRefresh,
            pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color(0xFF8D7B4B)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(items) { item ->
                ChooseItem(item.name, item.price.toString()) {
                    onClickItem(item.id)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ItemChildrenList(
    items: List<ItemState>,
    modifier: Modifier = Modifier,
    onClickItemChildren: (Int) -> Unit,
    pullRefreshState: PullRefreshState,
    isRefresh: Boolean,
) {
    Box(modifier = modifier.fillMaxSize()) {
        PullRefreshIndicator(
            isRefresh,
            pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color(0xFF8D7B4B)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(items) { item ->
                ChooseItem(item.name, item.price.toString()) {
                    onClickItemChildren(item.id)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ItemModifiersList(
    items: List<ItemModifierState>,
    modifier: Modifier = Modifier,
    onClickItemModifier: (Int) -> Unit,
    pullRefreshState: PullRefreshState,
    isRefresh: Boolean,
) {
    Box(modifier = modifier.fillMaxSize()) {
        PullRefreshIndicator(
            isRefresh,
            pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color(0xFF8D7B4B)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(items) { item ->
                ChooseItem(item.name, item.price.toString()) {
                    onClickItemModifier(item.id)
                }
            }
        }
    }
}