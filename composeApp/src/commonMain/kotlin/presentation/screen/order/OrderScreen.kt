package presentation.screen.order

import abapps_startouch.composeapp.generated.resources.Res
import abapps_startouch.composeapp.generated.resources.ic_back
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.beepbeep.designSystem.ui.composable.animate.FadeAnimation
import com.beepbeep.designSystem.ui.composable.animate.SlideAnimation
import com.beepbeep.designSystem.ui.theme.Theme
import domain.entity.FireItems
import kms
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf
import presentation.base.ErrorState
import presentation.screen.composable.AppButton
import presentation.screen.composable.AppDialogue
import presentation.screen.composable.AppTextField
import presentation.screen.composable.HandleErrorState
import presentation.screen.composable.SetLayoutDirection
import presentation.screen.composable.ShimmerListItem
import presentation.screen.composable.WarningDialogue
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
    private val items: List<FireItems>,
    private val isReopened: Boolean
) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val screenModel: OrderScreenModel =
            getScreenModel(parameters = { parametersOf(checkId, items) })
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
            WarningDialogue(
                Resources.strings.warning,
                "Item already exist do you want to add it again or alone",
                onDismissRequest = screenModel::onDismissWarningDialogue,
                onClickConfirmButton = { screenModel.add() },
                onClickDismissButton = screenModel::onDismissItemDialogue
            )
        }

        SetLayoutDirection(layoutDirection = LayoutDirection.Ltr) {
            Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Transparent,
                topBar = {
                    StAppBar(
                        onNavigateUp = {
                            if (state.presetItemsState.isNotEmpty() && !state.isPresetVisible && state.itemsState.isEmpty() && state.itemModifiersState.isEmpty() && !isReopened)
                                screenModel.showWarningDialogue()
                            else if (state.isFinishOrder) screenModel.onClickIconBack()
                            else if (!state.isPresetVisible && state.itemsState.isEmpty() && state.itemModifiersState.isEmpty() && items.isNotEmpty()) nav.replace(
                                DinInScreen()
                            )
                            else screenModel.backToPresets()
                        },
                        title = "Order #$checkId",
                        isBackIconVisible = state.itemModifiersState.isEmpty(),
                        painterResource = painterResource(Res.drawable.ic_back)
                    )
                },
                floatingActionButton = {
                    SlideAnimation(!state.isFinishOrder && state.itemModifiersState.isEmpty()) {
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
                    }
                }) {
                FadeAnimation(state.presetItemsState.isNotEmpty() && !state.isPresetVisible && state.itemsState.isEmpty() && state.itemModifiersState.isEmpty()) {
                    PresetsList(
                        state.presetItemsState,
                        onClickPreset = screenModel::onClickPreset,
                        modifier = Modifier.padding(top = it.calculateTopPadding())
                            .pullRefresh(pullRefreshState),
                        pullRefreshState = pullRefreshState,
                        isRefresh = state.isRefresh
                    )
                }
                SlideAnimation(state.isFinishOrder) {
                    OrdersList(
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
                                .pullRefresh(pullRefreshState)
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
                            modifier = Modifier.padding(top = it.calculateTopPadding())
                                .pullRefresh(pullRefreshState),
                        )
                    }
                    FadeAnimation(state.itemChildrenState.isNotEmpty()) {
                        ItemChildrenList(
                            state.itemChildrenState,
                            onClickItemChildren = screenModel::onClickItemChild,
                            modifier = Modifier.padding(top = it.calculateTopPadding())
                                .pullRefresh(pullRefreshState),
                        )
                    }
                    FadeAnimation(state.itemModifiersState.isNotEmpty()) {
                        ItemModifiersList(
                            state.itemModifiersState,
                            onClickItemModifier = screenModel::onClickItemModifier,
                            modifier = Modifier.padding(top = it.calculateTopPadding())
                                .pullRefresh(pullRefreshState),
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
        PullRefreshIndicator(
            isRefresh,
            pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color(0xFF8D7B4B)
        )
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
    }
}

@Composable
fun ItemCard(
    name: String,
    price: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(modifier.heightIn(260.dp).width(192.dp).bounceClick { onClick() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp))
                .background(Theme.colors.background)
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(DrawableResource("dish.png")),
                contentDescription = "",
                modifier = Modifier
                    .size(132.dp)
            )
            Text(
                text = name,
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                color = Theme.colors.contentPrimary,
                textAlign = TextAlign.Center,
                style = Theme.typography.titleLarge,
            )
            Text(
                text = price,
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                color = Theme.colors.contentPrimary,
                textAlign = TextAlign.Center,
                style = Theme.typography.titleLarge,
            )
        }
    }
}

@Composable
private fun ItemsList(
    items: List<ItemState>,
    modifier: Modifier = Modifier,
    onClickItem: (Int) -> Unit,
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
                    onClickItem(item.id)
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

@Composable
private fun OrdersList(
    orderItemState: List<OrderItemState>,
    orderInteractionListener: OrderInteractionListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().background(Color.White),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(orderItemState) {
                OrderItem(it, orderInteractionListener)
            }
            item {
                Row {
                    Text("Total Orders : ", fontSize = 20.sp)
                    Text(orderItemState.sumOf { it.qty }.toString(), fontSize = 20.sp)
                }
            }
            item {
                Row {
                    Text("Total Check price : ", fontSize = 20.sp)
                    Text(
                        orderItemState.sumOf { it.totalPrice.toDouble() }.toString(),
                        fontSize = 20.sp
                    )
                }
            }
            item {
                AppButton(
                    "Modify last item",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    containerColor = Color(0xFF202C59),
                    contentColor = Color.White
                ) {
                    orderInteractionListener.onClickModifyLastItem()
                }
            }
            item {
                AppButton(
                    "Fire items",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    containerColor = Color(0xFFFF4D8B),
                    contentColor = Color.White
                ) {
                    orderInteractionListener.onClickFire()
                }
            }
        }
    }
}

@Composable
private fun OrderItem(
    orderItemState: OrderItemState,
    orderInteractionListener: OrderInteractionListener,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(128.dp)
            .padding(8.dp),
        colors = if (!orderItemState.fired) CardDefaults.cardColors(Color.White) else CardDefaults.cardColors(
            Theme.colors.primary
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    orderItemState.name,
                    modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                Icon(
                    modifier = Modifier.size(32.dp)
                        .bounceClick { orderInteractionListener.onClickRemoveItem(orderItemState.id) },
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color(0xFFFF4D8B),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CounterCard(
                    orderItemState.qty.toString(),
                    orderItemState.id,
                    Modifier.fillMaxWidth(0.3f).align(Alignment.CenterVertically),
                    onClickMinus = orderInteractionListener::onClickMinus,
                    onClickPlus = orderInteractionListener::onClickPlus
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    orderItemState.totalPrice.toString(),
                    fontSize = 20.sp
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun CounterCard(
    text: String,
    id: Int,
    modifier: Modifier = Modifier,
    onClickPlus: (Int) -> Unit,
    onClickMinus: (Int) -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = { onClickMinus(id) },
                modifier = Modifier.size(48.dp)
            ) {
                Text(text = "-", fontSize = 24.sp, color = Color(0xFFFF4D8B))
            }
            Text(
                text = text,
                fontSize = 20.sp,
            )
            IconButton(
                onClick = { onClickPlus(id) },
                modifier = Modifier.size(48.dp)
            ) {
                Text(text = "+", fontSize = 24.sp, color = Color(0xFFFF4D8B))
            }
        }
    }
}

@Composable
private fun EnterModifyLastItemDialogue(
    comment: String,
    orderInteractionListener: OrderInteractionListener,
    modifier: Modifier = Modifier,
) {
    AppDialogue(
        onDismissRequest = orderInteractionListener::onDismissDialogue,
        modifier = modifier,
    ) {
        Text(
            "Enter comment",
            modifier = Modifier.padding(vertical = 8.kms),
            style = MaterialTheme.typography.headlineSmall
        )
        AppTextField(
            text = comment,
            onValueChange = orderInteractionListener::onModifyLastItemChanged,
            hint = Resources.strings.comment,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.kms),
            keyboardType = KeyboardType.Text,
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
                ) { orderInteractionListener.onDismissDialogue() }
                AppButton(
                    Resources.strings.ok,
                    modifier = Modifier.weight(1f),
                ) { orderInteractionListener.onClickOk() }
            }
        }
    }
}