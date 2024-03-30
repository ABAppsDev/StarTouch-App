package presentation.screen.order.items

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.StarTouchSetup
import domain.entity.Item
import domain.usecase.ManageOrderUseCase
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseScreenModel
import presentation.base.ErrorState
import presentation.screen.order.modifiers.toItemModifierState

class ItemsListScreenModel(
    private val manageOrder: ManageOrderUseCase,
    private val presetId: Int,
) : BaseScreenModel<ItemsListState, ItemsUiEffect>(ItemsListState()),
    ItemsInteractionListener {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        getAllItems()
    }

    private fun getAllItems() {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = "",
                errorState = null,
            )
        }
        tryToExecute(
            function = {
                manageOrder.getAllItems(
                    StarTouchSetup.OUTLET_ID,
                    StarTouchSetup.REST_ID,
                    presetId
                )
            },
            onSuccess = ::onGetAllItemsSuccess,
            onError = ::onGetAllItemsError
        )
    }

    private fun onGetAllItemsSuccess(items: List<Item>) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorState = null,
                itemsState = items.map { item ->
                    item.toItemState()
                }
            )
        }
    }

    private fun onGetAllItemsError(errorState: ErrorState) {
        updateState {
            it.copy(
                isLoading = false,
                errorState = errorState,
                itemsState = emptyList(),
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


    fun retry() {
        getAllItems()
    }


    override fun onClickItem(itemId: Int) {
        checkItemHasModifiers(itemId)
    }

    private fun checkItemHasModifiers(itemId: Int) {
        tryToExecute(
            function = {
                manageOrder.checkItemHasModifiers(
                    StarTouchSetup.REST_ID,
                    itemId
                )
            },
            onSuccess = ::onCheckItemSuccess,
            onError = ::onGetAllItemsError
        )
    }

    private fun onCheckItemSuccess(items: List<Item>) {
        if (items.isNotEmpty())
            sendNewEffect(ItemsUiEffect.NavigateToItemsModifierListScreen(items.map { it.toItemModifierState() }))
        else sendNewEffect(ItemsUiEffect.NavigateBackToPresetsListScreen)
    }
}