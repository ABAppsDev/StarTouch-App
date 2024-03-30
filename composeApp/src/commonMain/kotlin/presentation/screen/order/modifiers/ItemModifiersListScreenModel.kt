package presentation.screen.order.modifiers

import cafe.adriel.voyager.core.model.screenModelScope
import domain.entity.Item
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class ItemModifiersListScreenModel(
    // private val manageOrder: ManageOrderUseCase,
    private val items: List<ItemModifierState>,
) : BaseScreenModel<ItemModifiersListState, ItemModifiersUiEffect>(ItemModifiersListState()),
    ItemModifiersInteractionListener {
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
        launchDelayed(500) {
            updateState {
                it.copy(
                    isLoading = false,
                    errorMessage = "",
                    errorState = null,
                    itemModifiersState = items
                )
            }
        }
//        tryToExecute(
//            function = {
//                manageOrder.getAllItems(
//                    StarTouchSetup.OUTLET_ID,
//                    StarTouchSetup.REST_ID,
//                    itemId
//                )
//            },
//            onSuccess = ::onGetAllItemsSuccess,
//            onError = ::onGetAllItemsError
//        )
    }

    private fun onGetAllItemsSuccess(items: List<Item>) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorState = null,
                itemModifiersState = items.map { item ->
                    item.toItemModifierState()
                }
            )
        }
    }

    private fun onGetAllItemsError(errorState: ErrorState) {
        updateState {
            it.copy(
                isLoading = false,
                errorState = errorState,
                itemModifiersState = emptyList(),
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


    override fun onClickItemModifier(itemId: Int) {
        updateState { it.copy(isSuccess = true) }
        sendNewEffect(ItemModifiersUiEffect.NavigateBackToPresetsListScreen)
    }
}