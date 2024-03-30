package presentation.screen.order

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.StarTouchSetup
import domain.entity.Item
import domain.entity.Preset
import domain.usecase.ManageOrderUseCase
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class OrderScreenModel(
    private val manageOrder: ManageOrderUseCase,
    private val checkId: Int,
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
            onSuccess = ::onGetAllItemsSuccess,
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
                isPresetVisible = true,
                itemModifiersState = items.map { item ->
                    item.toItemModifierState()
                }
            )
        }
    }

    fun retry() {
        if (state.value.presetItemsState.isEmpty()) getAllPresets()
        else if (state.value.itemsState.isEmpty()) getAllItems(state.value.selectedPresetId)
        else if (state.value.itemChildrenState.isEmpty()) getAllItems(state.value.selectedItemId)
        else if (state.value.itemModifiersState.isEmpty()) getAllItemModifiers(state.value.selectedItemId)
    }

    override fun onClickPreset(presetId: Int) {
        getAllItems(presetId)
        updateState { it.copy(selectedPresetId = presetId) }
    }

    override fun onClickItemModifier(itemId: Int) {

    }

    override fun onClickItemChild(itemId: Int) {
        getAllItemModifiers(itemId)
        updateState { it.copy(selectedPresetId = itemId) }
    }

    override fun onClickItem(itemId: Int) {
        getAllItemModifiers(itemId)
        updateState { it.copy(selectedPresetId = itemId) }
    }

    override fun onClickFloatActionButton() {

    }

    override fun onClickFire() {

    }

    override fun onClickClose() {

    }

    override fun showErrorScreen() {
        updateState { it.copy(showErrorScreen = true) }
    }
}