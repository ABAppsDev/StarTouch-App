package presentation.screen.dinin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kms
import presentation.screen.composable.AppButton
import presentation.screen.composable.AppDialogue
import presentation.screen.composable.AppScaffold
import presentation.screen.composable.AppTextField
import presentation.screen.composable.AppThreeDotLoadingIndicator
import presentation.screen.composable.ErrorDialogue
import presentation.screen.composable.RestaurantTableWithText
import presentation.screen.composable.RestaurantTableWithTextLoading
import presentation.screen.composable.SetLayoutDirection
import presentation.screen.composable.ShimmerListItem
import presentation.screen.composable.WarningDialogue
import presentation.screen.composable.animate.FadeAnimation
import presentation.screen.composable.modifier.bounceClick
import presentation.screen.order.presets.PresetsListScreen
import presentation.util.EventHandler
import resource.Resources
import util.getScreenModel

class DinInScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val dinInScreenModel = getScreenModel<DinInScreenModel>()
        val state by dinInScreenModel.state.collectAsState()
        val pullRefreshState =
            rememberPullRefreshState(state.isRefreshing, { dinInScreenModel.retry() })

        EventHandler(dinInScreenModel.effect) { effect, navigator ->
            when (effect) {
                is DinInUiEffect.NavigateToPresetListScreen -> {
                    navigator.push(PresetsListScreen())
                }
            }
        }

        FadeAnimation(state.warningDialogueIsVisible) {
            WarningDialogue(
                Resources.strings.warning,
                Resources.strings.alreadyOpenChecks,
                onDismissRequest = dinInScreenModel::onDismissWarningDialogue,
                onClickConfirmButton = dinInScreenModel::onConfirmButtonClick,
                onClickDismissButton = dinInScreenModel::onDismissWarningDialogue
            )
        }

        LaunchedEffect(state.errorDinInState) {
            if (state.errorDinInState != null) dinInScreenModel.showErrorScreen()
        }
        FadeAnimation(visible = state.dinInDialogueState.isVisible) {
            DinInDialogue(
                covers = state.dinInDialogueState.coversCount,
                isLoadingButton = state.dinInDialogueState.isLoadingButton,
                isLoading = state.dinInDialogueState.isLoading,
                dinInInteractionListener = dinInScreenModel as DinInInteractionListener,
                assignDrawers = state.dinInDialogueState.assignDrawers,
                isSuccess = state.dinInDialogueState.isSuccess
            )
        }

        AppScaffold(
            isLoading = state.isLoading,
            onLoading = { ShimmerListItem { RestaurantTableWithTextLoading() } },
            error = state.errorState
        ) {
            LaunchedEffect(state.errorState) {
                if (state.errorState != null && state.errorMessage.isNotEmpty())
                    dinInScreenModel.showErrorDialogue()
            }
            FadeAnimation(state.errorDialogueIsVisible) {
                ErrorDialogue(
                    title = Resources.strings.error,
                    text = state.errorMessage,
                    onDismissRequest = dinInScreenModel::onDismissErrorDialogue,
                    onClickConfirmButton = dinInScreenModel::onDismissErrorDialogue,
                )
            }
            OnRender(state, dinInScreenModel as DinInInteractionListener, pullRefreshState)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun OnRender(
    state: DinInState,
    listener: DinInInteractionListener,
    pullRefreshState: PullRefreshState
) {
    Box(Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(state.tablesDetails) { table ->
                    ChooseTable(table) {
                        if (table.checksCount > 0)
                            listener.showWarningDialogue(table.tableId, table.tableNumber)
                        else
                            listener.onClickTable(table.tableId, table.tableNumber)
                    }
                }
            }
            PullRefreshIndicator(
                state.isRefreshing,
                pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = Color(0xFF8D7B4B)
            )
        }
    }
}


@Composable
private fun DinInDialogue(
    covers: String,
    isLoading: Boolean,
    isLoadingButton: Boolean,
    isSuccess: Boolean,
    assignDrawers: List<AssignDrawerState>,
    dinInInteractionListener: DinInInteractionListener,
    modifier: Modifier = Modifier,
) {
    AppDialogue(
        onDismissRequest = dinInInteractionListener::onDismissDinInDialogue,
        modifier = modifier
    ) {
        FadeAnimation(isLoading) {
            Box(
                Modifier.fillMaxWidth(LocalDensity.current.density / 1.5f)
                    .heightIn(min = 100.dp), contentAlignment = Alignment.Center
            ) {
                AppThreeDotLoadingIndicator(
                    dotColor = Color.White
                )
            }
        }
        if (isSuccess) {
            EnterCoversNumber(covers, isLoadingButton, dinInInteractionListener)
        }
        FadeAnimation(assignDrawers.isNotEmpty()) {
            ChooseListAssignDrawer(dinInInteractionListener, assignDrawers)
        }
    }
}

@Composable
private fun ChooseListAssignDrawer(
    dinInInteractionListener: DinInInteractionListener,
    assignDrawers: List<AssignDrawerState>,
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(assignDrawers) { assignDrawer ->
            AssignDrawerItem(assignDrawer.name) {
                dinInInteractionListener.onClickAssignDrawer(assignDrawer.id)
            }
        }
    }
}

@Composable
private fun AssignDrawerItem(
    name: String,
    onClick: () -> Unit,
) {
    SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
        Row(
            modifier = Modifier.fillMaxWidth().bounceClick { onClick() }.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(Color.White)) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = null,
                    Modifier.align(Alignment.Center).size(24.dp)
                )
            }
            Text(name, style = MaterialTheme.typography.headlineSmall, color = Color.White)
        }
    }
}

@Composable
private fun EnterCoversNumber(
    covers: String,
    isLoading: Boolean,
    dinInInteractionListener: DinInInteractionListener,
) {
    Text(
        Resources.strings.covers,
        modifier = Modifier.padding(vertical = 8.kms),
        style = MaterialTheme.typography.headlineSmall
    )
    AppTextField(
        text = covers,
        onValueChange = dinInInteractionListener::onCoversCountChanged,
        hint = Resources.strings.covers,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.kms),
        keyboardType = KeyboardType.Number,
    )
    SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.kms, vertical = 16.kms),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.kms)
        ) {
            AppButton(
                Resources.strings.cancel,
                modifier = Modifier.weight(1f),
            ) { dinInInteractionListener.onDismissDinInDialogue() }
            AppButton(
                Resources.strings.ok,
                modifier = Modifier.weight(1f),
                isLoading = isLoading,
            ) { dinInInteractionListener.onClickOk() }
        }
    }
}

@Composable
private fun ChooseTable(
    table: TableDetailsState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    RestaurantTableWithText(
        covers = table.covers.toString(),
        openTime = table.openCheckDate ?: "",
        tableCode = table.tableNumber.toString(),
        totalAmount = table.totalOrdersPrice.toString(),
        checksCount = table.checksCount.toString(),
        hasOrders = table.covers > 0 || table.openCheckDate != "null",
        modifier = modifier.bounceClick { onClick() }
    )
}