package presentation.screen.order

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.AppLanguage
import data.util.StarTouchSetup
import domain.entity.Item
import domain.entity.Preset
import domain.usecase.ManageChecksUseCase
import domain.usecase.ManageOrderUseCase
import domain.usecase.ManageSettingUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class OrderScreenModel(
    private val manageOrder: ManageOrderUseCase,
    private val manageChecksUseCase: ManageChecksUseCase,
    private val manageSetting: ManageSettingUseCase,
    private val checkId: Long,
) : BaseScreenModel<OrderState, OrderUiEffect>(OrderState()),
    OrderInteractionListener {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        getAllPresets()
    }

    private fun getAllPresets() {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = "",
                errorState = null,
                showErrorScreen = false,
                isPresetVisible = false,
                presetItemsState = emptyList()
            )
        }
        tryToExecute(
            function = {
                manageOrder.getAllPresets(
                    outletID = StarTouchSetup.OUTLET_ID,
                    restID = StarTouchSetup.REST_ID,
                    checkId = checkId
                )
            },
            onSuccess = ::onGetAllPresetsSuccess,
            onError = { error ->
                updateState { it.copy(presetItemsState = emptyList()) }
                onError(error)
            }
        )
    }

    private fun onGetAllPresetsSuccess(presets: List<Preset>) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorState = null,
                showErrorScreen = false,
                presetItemsState = presets.map { preset ->
                    preset.toPresetItemState()
                }
            )
        }
    }

    private fun onError(errorState: ErrorState) {
        updateState {
            it.copy(
                isLoading = false,
                errorState = errorState,
                errorMessage = when (errorState) {
                    is ErrorState.NetworkError -> errorState.message.toString()
                    is ErrorState.NotFound -> errorState.message.toString()
                    is ErrorState.ServerError -> errorState.message.toString()
                    is ErrorState.UnknownError -> errorState.message.toString()
                    is ErrorState.EmptyData -> errorState.message.toString()
                    else -> "Unknown error"
                }
            )
        }
        println(errorState.toString())
    }

    private fun getAllItems(presetId: Int) {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = "",
                errorState = null,
                itemsState = emptyList(),
                showErrorScreen = false,
            )
        }
        tryToExecute(
            function = {
                manageOrder.getAllItems(
                    StarTouchSetup.OUTLET_ID,
                    StarTouchSetup.REST_ID,
                    presetID = presetId,
                    checkId = checkId,
                    priceLvlId = StarTouchSetup.PRICE_LVL_ID,
                )
            },
            onSuccess = ::onGetAllItemsSuccess,
            onError = { error ->
                updateState { it.copy(itemsState = emptyList()) }
                onError(error)
            }
        )
    }

    private fun getAllItemChildren(itemId: Int) {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = "",
                errorState = null,
                itemChildrenState = emptyList(),
                showErrorScreen = false,
            )
        }
        tryToExecute(
            function = {
                manageOrder.checkItemHasChildren(
                    StarTouchSetup.REST_ID,
                    itemID = itemId,
                    priceLvlId = StarTouchSetup.PRICE_LVL_ID,
                )
            },
            onSuccess = ::onGetAllItemChildrenSuccess,
            onError = { error ->
                updateState { it.copy(itemChildrenState = emptyList()) }
                onError(error)
            }
        )
    }

    private fun onGetAllItemChildrenSuccess(items: List<Item>) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorState = null,
                isPresetVisible = true,
                itemChildrenState = items.map { item ->
                    item.toItemState()
                }
            )
        }
        if (state.value.itemChildrenState.isEmpty()) {
            getAllItemModifiers(state.value.selectedItemId)
        }
    }

    private fun onGetAllItemsSuccess(items: List<Item>) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorState = null,
                isPresetVisible = true,
                itemsState = items.map { item ->
                    item.toItemState()
                }
            )
        }
    }

    private fun getAllItemModifiers(selectedItemId: Int) {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = "",
                errorState = null,
                itemModifiersState = emptyList(),
                showErrorScreen = false,
            )
        }
        tryToExecute(
            function = {
                manageOrder.checkItemHasModifiers(
                    restID = StarTouchSetup.REST_ID,
                    itemID = selectedItemId,
                    priceLvlId = StarTouchSetup.PRICE_LVL_ID
                )
            },
            onSuccess = ::onGetAllItemsModifierSuccess,
            onError = { error ->
                updateState { it.copy(itemModifiersState = emptyList()) }
                onError(error)
            }
        )
    }

    private fun onGetAllItemsModifierSuccess(items: List<Item>) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorState = null,
                showErrorScreen = false,
                isPresetVisible = false,
                itemModifiersState = items.map { item ->
                    item.toItemModifierState()
                }
            )
        }
        if (state.value.itemModifiersState.isEmpty()) updateState { it.copy(isPresetVisible = false) }
    }

    fun retry() {
        getAllPresets()
    }

    override fun onClickPreset(presetId: Int) {
        getAllItems(presetId)
        updateState { it.copy(selectedPresetId = presetId) }
    }

    override fun onClickItemModifier(name: String) {
        println(state.value.itemModifiersState)
        val item = state.value.itemModifiersState.find { it.name == name }
        item?.let {
            addItem(
                OrderItemState(
                    id = item.id,
                    name = item.name,
                    qty = 1,
                    unitPrice = item.price,
                    isModifier = item.isModifier,
                    noServiceCharge = item.noServiceCharge,
                    modifierGroupID = item.modifierGroupID,
                    pickFollowItemQty = item.pickFollowItemQty,
                    prePaidCard = item.prePaidCard,
                    taxable = item.taxable,
                )
            )
            updateState { it.copy(orderItemState = orders.toList()) }
        }
        updateState {
            it.copy(
                selectedPresetId = 0,
                selectedItemId = 0,
                itemsState = emptyList(),
                itemChildrenState = emptyList(),
                itemModifiersState = emptyList(),
                isPresetVisible = false,
            )
        }
    }

    override fun onClickItemChild(itemId: Int) {
        val item = state.value.itemChildrenState.find { it.id == itemId }
        item?.let {
            addItem(
                OrderItemState(
                    id = item.id,
                    name = item.name,
                    qty = 1,
                    unitPrice = item.price,
                    isModifier = item.isModifier,
                    noServiceCharge = item.noServiceCharge,
                    modifierGroupID = item.modifierGroupID,
                    pickFollowItemQty = item.pickFollowItemQty,
                    prePaidCard = item.prePaidCard,
                    taxable = item.taxable,
                )
            )
            updateState { it.copy(orderItemState = orders.toList()) }
        }
        getAllItemModifiers(itemId)
        updateState { it.copy(selectedItemId = itemId, itemChildrenState = emptyList()) }
    }

    override fun onClickItem(itemId: Int) {
        val item = state.value.itemsState.find { it.id == itemId }
        item?.let {
            addItem(
                OrderItemState(
                    id = item.id,
                    name = item.name,
                    qty = 1,
                    unitPrice = item.price,
                    isModifier = item.isModifier,
                    noServiceCharge = item.noServiceCharge,
                    modifierGroupID = item.modifierGroupID,
                    pickFollowItemQty = item.pickFollowItemQty,
                    prePaidCard = item.prePaidCard,
                    taxable = item.taxable,
                )
            )
            updateState { it.copy(orderItemState = orders.toList()) }
        }
        getAllItemChildren(itemId)
        updateState { it.copy(selectedItemId = itemId, itemsState = emptyList()) }
    }

    override fun onClickFloatActionButton() {
        updateState {
            it.copy(
                isFinishOrder = true,
                isPresetVisible = false,
                itemsState = emptyList(),
                itemChildrenState = emptyList(),
                itemModifiersState = emptyList(),
                selectedItemId = 0,
                selectedPresetId = 0,
                presetItemsState = emptyList()
            )
        }
    }

    override fun onClickFire() {
        tryToExecute(
            function = {
                manageChecksUseCase.addItemsToCheck(
                    checkID = checkId,
                    userID = StarTouchSetup.USER_ID.toString(),
                    serverId = StarTouchSetup.REST_ID,
                    items = state.value.orderItemState.map { it.toEntity() }
                )
            },
            onSuccess = {
                if (it)
                    viewModelScope.launch {
                        val fastLoop = manageSetting.getIsBackToHome()
                        if (fastLoop) sendNewEffect(OrderUiEffect.NavigateBackToDinIn)
                        else {
                            AppLanguage.code.emit(StarTouchSetup.USER_LANGUAGE)
                            sendNewEffect(OrderUiEffect.NavigateBackToHome)
                        }
                    }
            },
            onError = ::onError
        )
    }

    override fun onClickClose() {

    }

    override fun showErrorScreen() {
        updateState { it.copy(showErrorScreen = true) }
    }

    override fun onClickIconBack() {
        updateState {
            it.copy(
                isFinishOrder = false,
                isPresetVisible = false,
                itemsState = emptyList(),
                itemChildrenState = emptyList(),
                itemModifiersState = emptyList(),
                selectedItemId = 0,
                selectedPresetId = 0,
                presetItemsState = emptyList()
            )
        }
        getAllPresets()
    }

    override fun onClickModifyLastItem() {
        updateState {
            it.copy(
                modifyLastItemDialogue = it.modifyLastItemDialogue.copy(
                    isVisible = true,
                    comment = ""
                )
            )
        }
    }

    override fun onDismissDialogue() {
        updateState {
            it.copy(
                modifyLastItemDialogue = it.modifyLastItemDialogue.copy(
                    isVisible = false
                )
            )
        }
    }

    override fun onModifyLastItemChanged(comment: String) {
        updateState {
            it.copy(
                modifyLastItemDialogue = it.modifyLastItemDialogue.copy(
                    comment = comment
                )
            )
        }
    }

    override fun onClickOk() {
        updateState {
            it.copy(
                modifyLastItemDialogue = it.modifyLastItemDialogue.copy(
                    isVisible = false
                )
            )
        }
    }

    override fun onClickMinus(id: Int) {
        val order = orders.find { it.id == id }
        order?.let { or ->
            if (or.qty == 1) {
                orders.remove(or)
                val newList = orders
                updateState {
                    it.copy(
                        orderItemState = newList.toList()
                    )
                }
            } else {
                orders[orders.indexOf(order)] =
                    or.copy(qty = or.qty - 1, totalPrice = (or.qty - 1) * or.unitPrice)
                val newList = orders
                updateState {
                    it.copy(
                        orderItemState = newList.toList()
                    )
                }
            }
        }
    }

    override fun addItem(orderItemState: OrderItemState) {
        orders.add(orderItemState)
    }

    override fun onClickPlus(id: Int) {
        val order = orders.find { it.id == id }
        order?.let { or ->
            orders[orders.indexOf(order)] =
                or.copy(qty = or.qty + 1, totalPrice = (or.qty + 1) * or.unitPrice)
            val newList = orders
            updateState {
                it.copy(
                    orderItemState = newList.toList()
                )
            }
        }
    }

    override fun onClickRemoveItem(id: Int) {
        val order = orders.find { it.id == id }
        orders.remove(order)
        val newList = orders
        updateState {
            it.copy(
                orderItemState = newList.toList()
            )
        }
    }
}