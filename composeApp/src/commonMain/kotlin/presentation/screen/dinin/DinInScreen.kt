package presentation.screen.dinin

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.ic_back
import abapps_startouch.composeapp.generated.resources.ic_profile_filled
import abapps_startouch.composeapp.generated.resources.invoice
import abapps_startouch.composeapp.generated.resources.table
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.beepbeep.designSystem.ui.composable.StAppBar
import com.beepbeep.designSystem.ui.composable.StButton
import com.beepbeep.designSystem.ui.composable.StChip
import com.beepbeep.designSystem.ui.composable.StDialogue
import com.beepbeep.designSystem.ui.composable.StOutlinedButton
import com.beepbeep.designSystem.ui.composable.StTextField
import com.beepbeep.designSystem.ui.composable.StThreeDotLoadingIndicator
import com.beepbeep.designSystem.ui.composable.animate.FadeAnimation
import com.beepbeep.designSystem.ui.theme.Theme
import org.jetbrains.compose.resources.painterResource
import presentation.screen.composable.AppScaffold
import presentation.screen.composable.ErrorDialogue
import presentation.screen.composable.RestaurantTableWithText
import presentation.screen.composable.RestaurantTableWithTextLoading
import presentation.screen.composable.SetLayoutDirection
import presentation.screen.composable.ShimmerListItem
import presentation.screen.composable.WarningDialogue
import presentation.screen.composable.extensions.bottomBorder
import presentation.screen.composable.modifier.bounceClick
import presentation.screen.order.OrderScreen
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
                is DinInUiEffect.NavigateToOrderScreen -> {
                    navigator.replace(
                        OrderScreen(
                            effect.checkId,
                            effect.checkNumber,
                            effect.items,
                            effect.isReopened
                        )
                    )
                }

                DinInUiEffect.NavigateBackToHome -> navigator.pop()
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
                assignChecks = state.dinInDialogueState.assignDrawers,
                isSuccess = state.dinInDialogueState.isSuccess,
                checks = state.dinInDialogueState.checks,
                isNamedTable = state.dinInDialogueState.isNamedTable,
                tableName = state.dinInDialogueState.tableName
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
            Column(Modifier.fillMaxSize()) {
                StAppBar(
                    onNavigateUp = listener::onClickBack,
                    painterResource = painterResource(Res.drawable.ic_back),
                    title = Resources.strings.dinningIn,
                )
                Row(
                    Modifier.fillMaxWidth()
                        .padding(top = 16.dp, bottom = 4.dp, start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StChip(
                        label = Resources.strings.createTableGuest,
                        isSelected = true,
                        onClick = { listener.onCreateTableGuest() },
                        painter = painterResource(Res.drawable.table)
                    )
                    StChip(
                        label = Resources.strings.showAllTableGuest,
                        isSelected = true,
                        onClick = { if (state.roomId != 0) listener.onClickTableGuest() },
                    )
                }
                LazyRow(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.align(Alignment.End)
                        .bottomBorder(1.dp, Theme.colors.divider),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    reverseLayout = true
                ) {
                    items(state.rooms) { room ->
                        StChip(
                            room.name,
                            isSelected = room.id == state.roomId,
                            onClick = { if (room.id != state.roomId) listener.onClickRoom(room.id) },
                            painter = painterResource(Res.drawable.table)
                        )
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(state.tablesDetails) { table ->
                        ChooseTable(
                            table,
                            onLongClick = {
                                if (state.roomId != 0)
                                    listener.onLongClick(table.tableId.toLong())
                                else listener.onLongClick(table.checkId ?: 0L)
                            },
                            id = state.roomId
                        ) {
                            if (table.checksCount > 0)
                                listener.showWarningDialogue(table.tableId, table.tableNumber)
                            else
                                listener.onClickTable(table.tableId, table.tableNumber)
                        }
                    }
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


@Composable
private fun DinInDialogue(
    covers: String,
    tableName: String,
    isLoading: Boolean,
    isLoadingButton: Boolean,
    isSuccess: Boolean,
    isNamedTable: Boolean,
    assignChecks: List<AssignCheckState>,
    checks: List<AssignCheckState>,
    dinInInteractionListener: DinInInteractionListener,
    modifier: Modifier = Modifier,
) {
    StDialogue(
        onDismissRequest = dinInInteractionListener::onDismissDinInDialogue,
        modifier = modifier
    ) {
        FadeAnimation(isLoading) {
            StThreeDotLoadingIndicator()
        }
        if (isSuccess) {
            EnterCoversNumber(covers, isLoadingButton, dinInInteractionListener)
        }
        if (isNamedTable) {
            EnterTableName(tableName, isLoadingButton, dinInInteractionListener)
        }
        FadeAnimation(assignChecks.isNotEmpty()) {
            ChooseListAssignCheck(dinInInteractionListener, assignChecks)
        }
        FadeAnimation(checks.isNotEmpty()) {
            ChooseCheck(dinInInteractionListener, checks)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnterTableName(
    tableName: String,
    loadingButton: Boolean,
    dinInInteractionListener: DinInInteractionListener
) {
    Text(
        text = Resources.strings.tableName,
        style = Theme.typography.headline,
        color = Theme.colors.contentPrimary,
    )
    StTextField(
        modifier = Modifier.padding(top = 24.dp),
        label = Resources.strings.tableName,
        text = tableName,
        hint = Resources.strings.tableName,
        onValueChange = dinInInteractionListener::onTableNameChanged,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Go,
        keyboardActions = KeyboardActions(onGo = {
            dinInInteractionListener.onClickOk()
        })
    )
    SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StOutlinedButton(
                title = Resources.strings.cancel,
                onClick = {
                    dinInInteractionListener.onDismissDinInDialogue()
                },
                modifier = Modifier.weight(1f)
            )
            StButton(
                title = Resources.strings.ok,
                onClick = {
                    dinInInteractionListener.onEnterTableName()
                },
                modifier = Modifier.weight(1f),
                isLoading = loadingButton,
            )
        }
    }
}

@Composable
private fun ChooseListAssignCheck(
    dinInInteractionListener: DinInInteractionListener,
    assignChecks: List<AssignCheckState>,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(assignChecks) { assignCheck ->
            AssignCheckItem(assignCheck.name) {
                dinInInteractionListener.onClickAssignCheck(assignCheck.id.toInt())
            }
        }
    }
}

@Composable
private fun ChooseCheck(
    dinInInteractionListener: DinInInteractionListener,
    checks: List<AssignCheckState>,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(checks) { check ->
            CheckItem(check.name) {
                dinInInteractionListener.onClickCheck(check.id, check.name.toInt())
            }
        }
    }
}

@Composable
private fun AssignCheckItem(
    name: String,
    onClick: () -> Unit,
) {
    SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
        Row(
            modifier = Modifier.fillMaxWidth().bounceClick { onClick() }.padding(8.dp)
                .bottomBorder(1.dp, Theme.colors.divider),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(48.dp).padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(Theme.radius.large))
                    .background(Theme.colors.surface)
            ) {
                Icon(
                    painterResource(Res.drawable.ic_profile_filled),
                    contentDescription = null,
                    Modifier.align(Alignment.Center).size(24.dp),
                    tint = Theme.colors.primary
                )
            }
            Text(name, style = Theme.typography.headline, color = Theme.colors.contentPrimary)
        }
    }
}

@Composable
private fun CheckItem(
    name: String,
    onClick: () -> Unit,
) {
    SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
        Row(
            modifier = Modifier.fillMaxWidth().bounceClick { onClick() }.padding(8.dp)
                .bottomBorder(1.dp, Theme.colors.divider),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(48.dp).padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(Theme.radius.large))
                    .background(Theme.colors.surface)
            ) {
                Icon(
                    painterResource(Res.drawable.invoice),
                    contentDescription = null,
                    Modifier.align(Alignment.Center).size(24.dp),
                )
            }
            Text(
                "Check number : $name",
                style = Theme.typography.headline,
                color = Theme.colors.contentPrimary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnterCoversNumber(
    covers: String,
    isLoading: Boolean,
    dinInInteractionListener: DinInInteractionListener,
) {
    Text(
        text = Resources.strings.covers,
        style = Theme.typography.headline,
        color = Theme.colors.contentPrimary,
    )
    StTextField(
        modifier = Modifier.padding(top = 24.dp),
        label = Resources.strings.covers,
        text = covers,
        hint = Resources.strings.covers,
        onValueChange = dinInInteractionListener::onCoversCountChanged,
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Go,
        keyboardActions = KeyboardActions(onGo = {
            dinInInteractionListener.onClickOk()
        })
    )
    SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StOutlinedButton(
                title = Resources.strings.cancel,
                onClick = {
                    dinInInteractionListener.onDismissDinInDialogue()
                },
                modifier = Modifier.weight(1f)
            )
            StButton(
                title = Resources.strings.ok,
                onClick = {
                    dinInInteractionListener.onClickOk()
                },
                modifier = Modifier.weight(1f),
                isLoading = isLoading,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChooseTable(
    table: TableDetailsState,
    id: Int,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    RestaurantTableWithText(
        covers = table.covers.toString(),
        openTime = table.openCheckDate ?: "",
        tableCode = table.tableNumber,
        totalAmount = table.totalOrdersPrice.toString(),
        checksCount = table.checksCount.toString(),
        hasOrders = table.covers > 0 || table.openCheckDate != "null",
        modifier = modifier
            .combinedClickable(
                onLongClick = {
                    if (table.covers > 0 && id != 0)
                        onClick()
                },
            ) {
                if (table.covers > 0) onLongClick() else if (id != 0) onClick()
            }
    )
}