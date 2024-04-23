package presentation.screen.order

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.ic_back
import abapps_startouch.composeapp.generated.resources.trash
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BadgedBox
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.popUntil
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.beepbeep.designSystem.ui.composable.StAppBar
import com.beepbeep.designSystem.ui.composable.StButton
import com.beepbeep.designSystem.ui.composable.StDialogue
import com.beepbeep.designSystem.ui.composable.StOutlinedButton
import com.beepbeep.designSystem.ui.composable.StTextField
import com.beepbeep.designSystem.ui.composable.animate.FadeAnimation
import com.beepbeep.designSystem.ui.composable.animate.SlideAnimation
import com.beepbeep.designSystem.ui.composable.snackbar.internal.SnackbarColor
import com.beepbeep.designSystem.ui.theme.Theme
import domain.entity.FireItems
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf
import presentation.base.ErrorState
import presentation.screen.composable.HandleErrorState
import presentation.screen.composable.SetLayoutDirection
import presentation.screen.composable.ShimmerListItem
import presentation.screen.composable.WarningDialogue
import presentation.screen.composable.WarningItemDialogue
import presentation.screen.composable.modifier.bounceClick
import presentation.screen.dinin.DinInScreen
import presentation.screen.home.HomeScreen
import presentation.screen.order.composable.ChooseItem
import presentation.screen.order.composable.ChoosePresetLoading
import presentation.util.EventHandler
import resource.Resources
import util.getScreenModel

class OrderScreen(
    private val checkId: Long,
    private val checkNumber: Int,
    private val items: List<FireItems>,
    private val isReopened: Boolean
) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val screenModel: OrderScreenModel =
            getScreenModel(parameters = { parametersOf(checkId, items, isReopened) })
        val state by screenModel.state.collectAsState()
        val pullRefreshState = rememberPullRefreshState(state.isRefresh, { screenModel.retry() })
        val nav = LocalNavigator.currentOrThrow

        EventHandler(screenModel.effect) { effect, navigator ->
            when (effect) {
                is OrderUiEffect.NavigateBackToDinIn -> {
                    navigator.replace(DinInScreen())
                }

                is OrderUiEffect.NavigateBackToHome -> {
                    navigator.popUntil<HomeScreen, Screen>()
                }
            }
        }


        FadeAnimation(visible = state.isLoading) {
            ShimmerListItem(columnsCount = 4) { ChoosePresetLoading() }
        }
        FadeAnimation(state.modifyLastItemDialogue.isVisible) {
            EnterModifyLastItemDialogue(
                state.modifyLastItemDialogue.comment,
                screenModel as OrderInteractionListener
            )
        }
        FadeAnimation(state.showEnterOpenPrice) {
            EnterPriceDialogue(
                state.price.toString(),
                screenModel as OrderInteractionListener
            )
        }
        LaunchedEffect(state.errorState) {
            if (state.errorState != null) screenModel.showErrorScreen()
        }
        FadeAnimation(state.warningDialogueIsVisible) {
            WarningDialogue(
                Resources.strings.warning,
                Resources.strings.doYouWantToAbortCheck,
                onDismissRequest = screenModel::onDismissWarningDialogue,
                onClickConfirmButton = { screenModel.abortedCheck(checkId) },
                onClickDismissButton = screenModel::onDismissWarningDialogue
            )
        }

        FadeAnimation(state.warningItemIsVisible) {
            WarningItemDialogue(
                Resources.strings.warning,
                Resources.strings.itemAlreadyExist,
                onDismissRequest = screenModel::onDismissWarningDialogue,
                onClickConfirmButton = { screenModel.addInExistItem() },
                onClickRejectButton = { screenModel.add() },
                onClickDismissButton = screenModel::onDismissItemDialogue
            )
        }

        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Transparent,
                topBar = {
                    StAppBar(
                        onNavigateUp = {
                            if (state.errorState != null || state.errorMessage != "" || state.showErrorScreen) screenModel.backToPresets()
                            else if (state.presetItemsState.isNotEmpty() && !state.isPresetVisible && state.itemsState.isEmpty() && state.itemModifiersState.isEmpty() && !isReopened)
                                screenModel.showWarningDialogue()
                            else if (state.isFinishOrder) screenModel.onClickIconBack()
                            else if (!state.isPresetVisible && state.itemsState.isEmpty() && state.itemModifiersState.isEmpty() && isReopened) nav.replace(
                                DinInScreen()
                            )
                            else screenModel.backToPresets()
                        },
                        title = "${Resources.strings.checkNumber} : $checkNumber",
                        isBackIconVisible = state.itemModifiersState.isEmpty(),
                        painterResource = painterResource(Res.drawable.ic_back)
                    )
                },
                floatingActionButton = {
                    SlideAnimation(!state.isFinishOrder && state.itemModifiersState.isEmpty()) {
                        Box {
                            FloatingActionButton(
                                onClick = screenModel::onClickFloatActionButton,
                                containerColor = Theme.colors.primary
                            ) {
                                Icon(
                                    Icons.Filled.ShoppingCart,
                                    contentDescription = null,
                                    tint = Theme.colors.contentPrimary
                                )
                            }
                            BadgedBox(
                                {
                                    Box(
                                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(
                                                    color = Theme.colors.contentPrimary,
                                                    shape = CircleShape
                                                ).align(Alignment.Center).padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                state.orderItemState.sumOf { it.qty.toDouble() }
                                                    .toInt().toString(),
                                                style = Theme.typography.title,
                                                modifier = Modifier.fillMaxSize()
                                                    .align(Alignment.Center),
                                                textAlign = TextAlign.Center,
                                                color = Theme.colors.surface
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.align(Alignment.TopCenter).padding(start = 8.dp)
                            ) {
                            }
                        }
                    }
                })
            {
                FadeAnimation(state.presetItemsState.isNotEmpty() && !state.isPresetVisible && state.itemsState.isEmpty() && state.itemModifiersState.isEmpty() && state.errorState == null && !state.isLoading) {
                    PresetsList(
                        state.presetItemsState,
                        onClickPreset = screenModel::onClickPreset,
                        modifier = Modifier.padding(top = it.calculateTopPadding())
                            .pullRefresh(pullRefreshState),
                        pullRefreshState = pullRefreshState,
                        isRefresh = state.isRefresh
                    )
                }
                FadeAnimation(state.showErrorScreen) {
                    HandleErrorState(
                        title = state.errorMessage,
                        error = state.errorState
                            ?: ErrorState.UnknownError(Resources.strings.somethingWrongHappened),
                        onClick = screenModel::retry
                    )
                }
                SlideAnimation(state.isFinishOrder) {
                    OrdersList(
                        isLoading = state.isLoadingButton,
                        orderItemState = state.orderItemState,
                        screenModel as OrderInteractionListener,
                        modifier = Modifier.padding(top = it.calculateTopPadding())
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FadeAnimation(state.isPresetVisible && state.presetItemsState.isNotEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = it.calculateTopPadding())

                        ) {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(24.dp),
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
                    FadeAnimation(state.itemsState.isNotEmpty()) {
                        ItemsList(
                            state.itemsState,
                            onClickItem = screenModel::onClickItem,
                            id = state.selectedItemId,
                            onChooseItem = screenModel::onChooseItem,
                            showOpenPriceDialogue = screenModel::showPriceDialogue
                            //modifier = Modifier.padding(top = it.calculateTopPadding()),
                        )
                    }
                    FadeAnimation(state.itemChildrenState.isNotEmpty()) {
                        ItemChildrenList(
                            state.itemChildrenState,
                            onClickItemChildren = screenModel::onClickItemChild,
                            modifier = Modifier.padding(top = it.calculateTopPadding()),
                        )
                    }
                    FadeAnimation(state.itemModifiersState.isNotEmpty()) {
                        ItemModifiersList(
                            state.itemModifiersState,
                            onClickItemModifier = screenModel::onClickItemModifier,
                            modifier = Modifier.padding(top = it.calculateTopPadding()),
                        )
                    }
                }
            }
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
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
        PullRefreshIndicator(
            isRefresh,
            pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color(0xFF8D7B4B)
        )
    }
}

@Composable
fun ItemCard(
    name: String,
    price: String,
    modifier: Modifier = Modifier,
    openPrice: Boolean = false,
    id: Int = 0,
    isChoose: Boolean = false,
    showOpenPriceDialogue: (Int, Float) -> Unit = { _, _ -> },
    onClickOk: (Int, Float) -> Unit = { _, _ -> },
    onClick: () -> Unit,
) {
    var qty by remember { mutableStateOf("1") }
    val source = remember {
        MutableInteractionSource()
    }

    if (source.collectIsPressedAsState().value) {
        qty = ""
    }
    Box(
        modifier.heightIn(100.dp)
            .width(192.dp)
            .background(Theme.colors.disable, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp)
            .bounceClick { onClick();qty = "1" }) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.8f)
//                .align(Alignment.BottomCenter)
//                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp))
//                .background(Theme.colors.surface)
//        )
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
//            Image(
//                painter = painterResource(DrawableResource("dish.png")),
//                contentDescription = "",
//                modifier = Modifier
//                    .size(132.dp)
//                    .align(Alignment.Start)
//            )
            Text(
                text = name,
                maxLines = 1,
                color = Theme.colors.contentPrimary,
                style = Theme.typography.title,
            )
            SlideAnimation(!isChoose) {
                Text(
                    text = price,
                    maxLines = 1,
                    color = Theme.colors.contentPrimary,
                    style = Theme.typography.title,
                )
            }
            SlideAnimation(isChoose) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .heightIn(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF2D303E)),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEFE3C8))
                                    .clickable {
                                        if (qty.isEmpty() || qty.isBlank()) return@clickable
                                        if (qty.toFloat() > 1f) {
                                            var temp = qty.toFloat()
                                            temp -= 1f
                                            qty = temp.toString()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "-",
                                    color = Theme.colors.surface,
                                    style = Theme.typography.titleMedium
                                )
                            }
                            BasicTextField(
                                value = qty,
                                onValueChange = {
                                    qty = it
                                },
                                textStyle = TextStyle(
                                    color = Theme.colors.contentPrimary,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(horizontal = 4.dp),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    onClickOk(
                                        id,
                                        qty.toFloat()
                                    )
                                    qty = "1"
                                }),
                                interactionSource = source
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEFE3C8))
                                    .clickable {
                                        if (qty.isEmpty() || qty.isBlank()) return@clickable
                                        if (qty.toFloat() < 99f) {
                                            var temp = qty.toFloat()
                                            temp += 1
                                            qty = temp.toString()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    color = Theme.colors.surface,
                                    style = Theme.typography.titleMedium
                                )
                            }
                            IconButton(modifier = Modifier.weight(1.5f).padding(start = 2.dp),
                                onClick = {
                                    if (qty.isEmpty() || qty.isBlank()) return@IconButton
                                    if (openPrice) showOpenPriceDialogue(id, qty.toFloat())
                                    else onClickOk(id, qty.toFloat())
                                    qty = "1"
                                }) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    modifier = Modifier.size(80.dp),
                                    contentDescription = null,
                                    tint = Theme.colors.success
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemsList(
    items: List<ItemState>,
    id: Int = 0,
    modifier: Modifier = Modifier,
    onClickItem: (Int, Float) -> Unit,
    showOpenPriceDialogue: (Int, Float) -> Unit,
    onChooseItem: (Int) -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(items) { item ->
                ItemCard(
                    name = item.name,
                    price = item.price.toString(),
                    id = item.id,
                    isChoose = id == item.id,
                    openPrice = item.openPrice,
                    showOpenPriceDialogue = showOpenPriceDialogue,
                    onClickOk = onClickItem
                ) {
                    onChooseItem(item.id)
                }
            }
        }
    }
}

@Composable
private fun ItemChildrenList(
    items: List<ItemState>,
    modifier: Modifier = Modifier,
    onClickItemChildren: (Int) -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(items) { item ->
                ItemCard(item.name, item.price.toString()) {
                    onClickItemChildren(item.id)
                }
            }
        }
    }
}

@Composable
private fun ItemModifiersList(
    items: List<ItemModifierState>,
    modifier: Modifier = Modifier,
    onClickItemModifier: (String) -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(items) { item ->
                ItemCard(item.name, item.price.toString()) {
                    onClickItemModifier(item.name)
                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun OrdersList(
    isLoading: Boolean,
    orderItemState: List<OrderItemState>,
    orderInteractionListener: OrderInteractionListener,
    modifier: Modifier = Modifier,
) {
    FadeAnimation(orderItemState.isEmpty()) {
        CardEmpty(Modifier.fillMaxSize())
    }
    FadeAnimation(orderItemState.isNotEmpty()) {
        val scope = rememberCoroutineScope()
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF1F1D2B))
                .padding(16.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = Resources.strings.item,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        style = Theme.typography.titleMedium,
                    )
                    Row(
                        modifier = Modifier.weight(0.5f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = Resources.strings.quantity,
                            color = Color.White,
                            style = Theme.typography.titleMedium
                        )
                        Text(
                            text = Resources.strings.price,
                            color = Color.White,
                            style = Theme.typography.titleMedium
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                        .height(1.dp)
                        .background(color = Color(0xFF393C49))
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(orderItemState, key = { it.serial }) { item ->
                        val dismissState = rememberDismissState(
                            initialValue = DismissValue.Default,
                        )
                        SwipeToDismiss(
                            modifier = Modifier
                                .animateItemPlacement()
                                .clip(RoundedCornerShape(16.dp)),
                            state = dismissState,
                            background = {
                                if (!item.fired && !item.voided) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .fillMaxHeight()
                                            .background(Color.Transparent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row {
                                            IconButton(onClick = { scope.launch { dismissState.reset() } }) {
                                                Icon(
                                                    Icons.Default.Refresh,
                                                    contentDescription = "Refresh",
                                                    tint = Color.White
                                                )
                                            }
                                            if (dismissState.targetValue == DismissValue.DismissedToStart)
                                                IconButton(onClick = {
                                                    if (item.voided || item.fired) return@IconButton
                                                    orderInteractionListener.onClickRemoveItem(item.serial)
                                                }) {
                                                    Icon(
                                                        painterResource(Res.drawable.trash),
                                                        contentDescription = "Delete",
                                                        tint = SnackbarColor.Error
                                                    )
                                                }
                                        }
                                    }
                                }
                            },
                            directions = if (!item.fired && !item.voided) setOf(DismissDirection.EndToStart) else setOf(),
                            dismissContent = {
                                OrderItem(
                                    title = item.name,
                                    qty = item.qty,
                                    qtyDecrease = orderInteractionListener::onClickMinus,
                                    qtyIncrease = orderInteractionListener::onClickPlus,
                                    onClickModify = orderInteractionListener::onClickModifyLastItem,
                                    totalPrice = item.totalPrice,
                                    singlePrice = item.unitPrice,
                                    id = item.id,
                                    serial = item.serial,
                                    voided = item.voided,
                                    fired = item.fired,
                                    isModifier = item.isModifier
                                )
                            }
                        )
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.BottomCenter)
                                .background(Color(0xFF1F1D2B)),
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp)
                                    .height(1.dp)
                                    .background(color = Color(0xFF393C49))
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = Resources.strings.totalItems,
                                    color = Color.LightGray,
                                    style = Theme.typography.title
                                )
                                Text(
                                    text = orderItemState.sumOf { it.qty.toDouble() }.toFloat()
                                        .toString(),
                                    color = Color.White,
                                    style = Theme.typography.titleMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = Resources.strings.totalCheckPrice,
                                    color = Color.LightGray,
                                    style = Theme.typography.title
                                )
                                Text(
                                    text = "${
                                        orderItemState.sumOf { it.totalPrice.toDouble() }.toFloat()
                                    }",
                                    color = Color.White,
                                    style = Theme.typography.titleMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(42.dp))
                            StButton(
                                title = Resources.strings.fire,
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = Theme.colors.primary,
                                onClick = {
                                    if (!isLoading)
                                        orderInteractionListener.onClickFire()
                                },
                                enabled = !orderItemState.all { it.fired },
                                isLoading = isLoading
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderItem(
    title: String,
    singlePrice: Float,
    totalPrice: Float,
    qty: Float,
    id: Int,
    serial: Int,
    qtyIncrease: (Int) -> Unit,
    qtyDecrease: (Int) -> Unit,
    onClickModify: (Int, Int) -> Unit,
    voided: Boolean,
    fired: Boolean,
    isModifier: Boolean,
    modifier: Modifier = Modifier
) {
    val cardColor = if (voided) SnackbarColor.Info
    else if (fired) SnackbarColor.Error
    else Color(0xFF1F1D2B)
    Card(
        modifier = modifier.bounceClick {
            if (voided || fired || isModifier) return@bounceClick
            onClickModify(id, serial)
        },
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    painter = painterResource(DrawableResource("dish.png")),
                    contentDescription = "item image"
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = title,
                        color = Color.White,
                        style = Theme.typography.title
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$singlePrice",
                        color = Theme.colors.contentSecondary,
                        style = Theme.typography.titleMedium
                    )
                }
            }
            Row(
                modifier = Modifier.weight(0.5f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (voided || fired || isModifier) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF393C49))
                            .background(Color(0xFF2D303E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isModifier) "${qty}M" else qty.toString(),
                            color = Color.White,
                            style = Theme.typography.titleMedium,
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF2D303E)),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEFE3C8))
                                    .clickable {
                                        if (voided || fired || isModifier) return@clickable
                                        qtyDecrease(serial)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "-",
                                    color = Color.Black,
                                    style = Theme.typography.title,
                                )
                            }
                            Text(
                                text = qty.toString(),
                                color = Color.White,
                                style = Theme.typography.title,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEFE3C8))
                                    .clickable {
                                        if (voided || fired || isModifier) return@clickable
                                        qtyIncrease(serial)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    color = Color.Black,
                                    style = Theme.typography.title
                                )
                            }
                        }
                    }
                }
                Text(
                    text = "$totalPrice",
                    color = Color.White,
                    style = Theme.typography.titleMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnterModifyLastItemDialogue(
    comment: String,
    orderInteractionListener: OrderInteractionListener,
    modifier: Modifier = Modifier,
) {
    StDialogue(
        onDismissRequest = orderInteractionListener::onDismissDialogue,
        modifier = modifier,
    ) {
        Text(
            text = Resources.strings.enterComment,
            style = Theme.typography.headline,
            color = Theme.colors.contentPrimary,
        )
        StTextField(
            modifier = Modifier.padding(top = 24.dp),
            label = "",
            text = comment,
            hint = Resources.strings.comment,
            onValueChange = orderInteractionListener::onModifyLastItemChanged,
            imeAction = ImeAction.Go,
            keyboardActions = KeyboardActions(onGo = {
                orderInteractionListener.onClickOk()
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
                        orderInteractionListener.onDismissDialogue()
                    },
                    modifier = Modifier.weight(1f)
                )
                StButton(
                    title = Resources.strings.ok,
                    onClick = {
                        orderInteractionListener.onClickOk()
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnterPriceDialogue(
    price: String,
    orderInteractionListener: OrderInteractionListener,
    modifier: Modifier = Modifier,
) {
    StDialogue(
        onDismissRequest = orderInteractionListener::onDismissPriceDialogue,
        modifier = modifier,
    ) {
        Text(
            text = Resources.strings.openPrice,
            style = Theme.typography.headline,
            color = Theme.colors.contentPrimary,
        )
        StTextField(
            modifier = Modifier.padding(top = 24.dp),
            label = "",
            text = price,
            hint = Resources.strings.enterPrice,
            onValueChange = orderInteractionListener::onPriceChanged,
            imeAction = ImeAction.Go,
            keyboardType = KeyboardType.Number,
            keyboardActions = KeyboardActions(onGo = {
                orderInteractionListener.onClickOkPrice()
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
                        orderInteractionListener.onDismissPriceDialogue()
                    },
                    modifier = Modifier.weight(1f)
                )
                StButton(
                    title = Resources.strings.ok,
                    onClick = {
                        orderInteractionListener.onClickOkPrice()
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}