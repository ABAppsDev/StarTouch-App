package presentation.screen.order

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.AppLanguage
import data.util.StarTouchSetup
import domain.entity.FireItems
import domain.entity.Item
import domain.entity.Preset
import domain.usecase.ManageChecksUseCase
import domain.usecase.ManageOrderUseCase
import domain.usecase.ManageSettingUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import presentation.base.BaseScreenModel
import presentation.base.ErrorState
import kotlin.random.Random

class OrderScreenModel(
    private val manageOrder: ManageOrderUseCase,
    private val manageChecksUseCase: ManageChecksUseCase,
    private val manageSetting: ManageSettingUseCase,
    private val checkId: Long,
    items: List<FireItems>,
    private val isReopened: Boolean,
) : BaseScreenModel<OrderState, OrderUiEffect>(OrderState()),
    OrderInteractionListener {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        orders.clear()
        updateState { it.copy(orderItemState = orders.toList()) }
        getAllPresets()
        orders.addAll(items.map { it.toState() })
        val newList = orders.toList()
        updateState { it.copy(orderItemState = newList) }
    }

    fun backToPresets() {
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

    fun abortedCheck(checkId: Long) {
        tryToExecute(
            function = {
                manageChecksUseCase.makeCheckAborted(
                    checkId,
                    StarTouchSetup.OUTLET_ID,
                    StarTouchSetup.REST_ID
                )
            },
            onSuccess = {
                updateState { it.copy(orderItemState = emptyList()) }
                viewModelScope.launch {
                    val fastLoop = manageSetting.getIsBackToHome()
                    if (fastLoop) sendNewEffect(OrderUiEffect.NavigateBackToDinIn)
                    else {
                        AppLanguage.code.emit(StarTouchSetup.DEFAULT_LANGUAGE)
                        sendNewEffect(OrderUiEffect.NavigateBackToHome)
                    }
                }
            },
            onError = ::onError
        )
    }

    private fun getAllPresets() {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = "",
                errorState = null,
                showErrorScreen = false,
                isPresetVisible = false,
                presetItemsState = emptyList(),
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
                    is ErrorState.ValidationNetworkError -> errorState.message.toString()
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
                    StarTouchSetup.OUTLET_ID,
                    itemID = itemId,
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
                },
            )
        }
        if (items.isEmpty()) {
            addItem(state.value.selectedItemId)
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
                    outletID = StarTouchSetup.OUTLET_ID
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
                itemsState = if (items.isNotEmpty()) emptyList() else state.value.itemsState,
                itemChildrenState = emptyList(),
                itemModifiersState = items.map { item ->
                    item.toItemModifierState()
                }
            )
        }
        if (state.value.itemModifiersState.isEmpty()) updateState {
            it.copy(
                isPresetVisible = true,
                qty = 0f
            )
        }
    }

    fun retry() {
        getAllPresets()
    }

    override fun onClickPreset(presetId: Int) {
        getAllItems(presetId)
        updateState { it.copy(selectedPresetId = presetId) }
    }

    override fun onClickItemModifier(name: String) {
        val item = state.value.itemModifiersState.find { it.name == name }
        item?.let {
            val order = orders.find { it.id == state.value.selectedItemId }
            val serial =
                orders.indexOf(order) + 1
            val x = Random.nextInt()
            addItem(
                OrderItemState(
                    id = item.id,
                    serial = x,
                    name = item.name,
                    counter = serial + 1,
                    qty = state.value.qty,
                    unitPrice = item.price,
                    isModifier = item.isModifier,
                    noServiceCharge = item.noServiceCharge,
                    refModItem = serial,
                    status = item.name,
                    refItemId = order?.serial ?: 0,
                    modifierGroupID = item.modifierGroupID,
                    pickFollowItemQty = item.pickFollowItemQty,
                    prePaidCard = item.prePaidCard,
                    taxable = item.taxable,
                    pOnReport = item.pOnReport,
                    pOnCheck = item.pOnCheck
                )
            )
            updateState { it.copy(orderItemState = orders.toList(), qty = 0f) }
        }
        updateState {
            it.copy(
                selectedPresetId = 0,
                selectedItemId = 0,
                itemId = 0,
                itemsState = emptyList(),
                itemChildrenState = emptyList(),
                itemModifiersState = emptyList(),
                isPresetVisible = false,
            )
        }
    }

    private fun addItem(itemId: Int) {
        val serial =
            orders.indexOf(orders.find { it.id == state.value.modifyLastItemDialogue.itemId }) + 1
        val item = state.value.itemsState.find { it.id == itemId }
        if (orders.contains(orders.find { it.id == item?.id }))
            showWarningItem()
        else {
            item?.let {
                addItem(
                    OrderItemState(
                        id = item.id,
                        serial = Random.nextInt(),
                        name = item.name,
                        qty = state.value.qty,
                        counter = serial,
                        unitPrice = item.price,
                        isModifier = item.isModifier,
                        noServiceCharge = item.noServiceCharge,
                        modifierGroupID = item.modifierGroupID,
                        pickFollowItemQty = item.pickFollowItemQty,
                        prePaidCard = item.prePaidCard,
                        taxable = item.taxable,
                        status = "Preparing",
                        fired = false,
                        voided = false,
                        modifierPick = 0,
                        refModItem = 0,
                        pOnReport = item.pOnReport,
                        pOnCheck = item.pOnCheck,
                    )
                )
                updateState { it.copy(orderItemState = orders.toList()) }
            }
            getAllItemModifiers(itemId)
        }
    }

    override fun onClickItemChild(itemId: Int) {
        updateState { it.copy(selectedItemId = itemId) }
        val serial =
            orders.indexOf(orders.find { it.id == state.value.modifyLastItemDialogue.itemId }) + 1
        val item = state.value.itemChildrenState.find { it.id == itemId }
        if (orders.contains(orders.find { it.id == item?.id }))
            showWarningItem()
        else {
            item?.let {
                addItem(
                    OrderItemState(
                        id = item.id,
                        name = item.name,
                        serial = Random.nextInt(),
                        counter = serial,
                        qty = state.value.qty,
                        unitPrice = item.price,
                        isModifier = item.isModifier,
                        noServiceCharge = item.noServiceCharge,
                        modifierGroupID = item.modifierGroupID,
                        pickFollowItemQty = item.pickFollowItemQty,
                        prePaidCard = item.prePaidCard,
                        taxable = item.taxable,
                        status = "Preparing",
                        fired = false,
                        voided = false,
                        modifierPick = 0,
                        refModItem = 0,
                        pOnReport = item.pOnReport,
                        pOnCheck = item.pOnCheck,
                    )
                )
                updateState { it.copy(orderItemState = orders.toList()) }
            }
            getAllItemModifiers(itemId)
            updateState { it.copy(selectedItemId = itemId) }
        }
    }

    fun add() {
        getAllItemModifiers(state.value.selectedItemId)
        val serial =
            orders.indexOf(orders.find { it.id == state.value.modifyLastItemDialogue.itemId }) + 1
        val item = state.value.itemsState.find { it.id == state.value.selectedItemId }
        item?.let {
            addItem(
                OrderItemState(
                    id = item.id,
                    name = item.name,
                    qty = state.value.qty,
                    counter = serial,
                    serial = Random.nextInt(),
                    unitPrice = item.price,
                    isModifier = item.isModifier,
                    noServiceCharge = item.noServiceCharge,
                    modifierGroupID = item.modifierGroupID,
                    pickFollowItemQty = item.pickFollowItemQty,
                    prePaidCard = item.prePaidCard,
                    taxable = item.taxable,
                    status = "Preparing",
                    fired = false,
                    voided = false,
                    modifierPick = 0,
                    refModItem = 0,
                    pOnReport = item.pOnReport,
                    pOnCheck = item.pOnCheck,
                )
            )
            updateState {
                it.copy(
                    orderItemState = orders.toList(),
                    warningItemIsVisible = false,
                    isPresetVisible = true,
                )
            }
        }
        getAllItemModifiers(state.value.selectedItemId)
    }

    override fun onClickItem(itemId: Int, qty: Float) {
        updateState { it.copy(selectedItemId = itemId, qty = qty) }
        getAllItemChildren(itemId)
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
                itemId = 0,
                selectedPresetId = 0,
                presetItemsState = emptyList()
            )
        }
    }

    override fun onClickFire() {
        updateState { it.copy(isLoadingButton = true) }
        orders.forEachIndexed { i, order ->
            if (order.isModifier) {
                val temp = orders.find { it.serial == order.refItemId }
                val item = orders.indexOf(orders.find {
                    it.serial == order.refItemId
                })
                orders[i] =
                    order.copy(
                        refModItem = if (item == 0) 1 else item + 1,
                        qty = if (order.qty > 0f) temp?.qty ?: 1f else order.qty
                    )
                val newList = orders
                updateState {
                    it.copy(
                        orderItemState = newList.toList()
                    )
                }
            }
        }
        tryToExecute(
            function = {
                val fireItems =
                    state.value.orderItemState.filter { !it.fired }.map { it.toEntity() }
                if (isReopened) manageChecksUseCase.addItemsToExistCheck(
                    checkID = checkId,
                    userID = StarTouchSetup.USER_ID.toString(),
                    serverId = StarTouchSetup.REST_ID,
                    items = fireItems
                ) else
                    manageChecksUseCase.addItemsToCheck(
                        checkID = checkId,
                        userID = StarTouchSetup.USER_ID.toString(),
                        serverId = StarTouchSetup.REST_ID,
                        items = fireItems
                    )
            },
            onSuccess = {
                if (it)
                    viewModelScope.launch {
                        updateState { state -> state.copy(orderItemState = emptyList()) }
                        orders.clear()
                        val fastLoop = manageSetting.getIsBackToHome()
                        updateState { s -> s.copy(isLoadingButton = false) }
                        if (fastLoop) sendNewEffect(OrderUiEffect.NavigateBackToDinIn)
                        else {
                            AppLanguage.code.emit(StarTouchSetup.DEFAULT_LANGUAGE)
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
                itemId = 0,
                selectedPresetId = 0,
                presetItemsState = emptyList()
            )
        }
        getAllPresets()
    }

    override fun onClickModifyLastItem(id: Int, serial: Int) {
        updateState {
            it.copy(
                modifyLastItemDialogue = it.modifyLastItemDialogue.copy(
                    isVisible = true,
                    comment = "",
                    itemId = id,
                    serial = serial,
                )
            )
        }
    }

    override fun onChooseItem(itemId: Int) {
        updateState { it.copy(selectedItemId = itemId) }
    }

    override fun onDismissDialogue() {
        updateState {
            it.copy(
                modifyLastItemDialogue = it.modifyLastItemDialogue.copy(
                    isVisible = false,
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
        //== ids[state.value.modifyLastItemDialogue.itemId]
        val order =
            orders.find { it.serial == state.value.modifyLastItemDialogue.serial }
        val serial =
            orders.indexOf(order) + 1
        orders.add(
            serial,
            OrderItemState(
                id = 0,
                name = state.value.modifyLastItemDialogue.comment,
                qty = 0f,
                unitPrice = 0f,
                isModifier = true,
                noServiceCharge = false,
                modifierGroupID = 0,
                counter = serial + 1,
                pickFollowItemQty = false,
                prePaidCard = false,
                refItemId = state.value.modifyLastItemDialogue.serial,
                taxable = false,
                modifierPick = 1,
                serial = Random.nextInt(),
                status = state.value.modifyLastItemDialogue.comment,
                refModItem = order?.serial ?: serial,
                pOnCheck = true,
                pOnReport = false,
                totalPrice = 0f
            )
        )
        updateState { it.copy(orderItemState = orders.toList()) }
        updateState {
            it.copy(
                modifyLastItemDialogue = it.modifyLastItemDialogue.copy(
                    isVisible = false
                )
            )
        }
    }

    override fun onClickMinus(id: Int) {
        val order = orders.find { it.serial == id && !it.fired && !it.voided && !it.isModifier }
        order?.let { or ->
            if (or.qty == 1f) {
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

    override fun showWarningDialogue() {
        updateState { it.copy(warningDialogueIsVisible = true) }
    }

    override fun onDismissWarningDialogue() {
        updateState { it.copy(warningDialogueIsVisible = false) }
    }

    override fun onDismissItemDialogue() {
        updateState { it.copy(warningItemIsVisible = false) }
    }

    override fun showWarningItem() {
        updateState { it.copy(warningItemIsVisible = true) }
    }

    override fun onClickPlus(id: Int) {
        val order = orders.find { it.serial == id && !it.fired && !it.voided && !it.isModifier }
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
        val order = orders.find { it.serial == id && !it.fired && !it.voided }
        orders.forEachIndexed { i, order2 ->
            if (order2.isModifier) {
                val temp = orders.find { it.serial == order2.refItemId }
                val item = orders.indexOf(orders.find {
                    it.serial == order2.refItemId
                })
            }
        }
        orders.remove(order)
        val newList = orders
        updateState {
            it.copy(
                orderItemState = newList.toList()
            )
        }
    }
}