package presentation.screen.order.presets

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.StarTouchSetup
import domain.entity.Preset
import domain.usecase.ManageOrderUseCase
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class PresetsListScreenModel(
    private val manageOrder: ManageOrderUseCase
) : BaseScreenModel<PresetsListState, PresetsUiEffect>(PresetsListState()),
    PresetsInteractionListener {
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
                showErrorScreen = false
            )
        }
        tryToExecute(
            function = {
                manageOrder.getAllPresets(
                    StarTouchSetup.OUTLET_ID,
                    StarTouchSetup.REST_ID
                )
            },
            onSuccess = ::onGetAllPresetsSuccess,
            onError = ::onGetAllPresetsError
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

    private fun onGetAllPresetsError(errorState: ErrorState) {
        updateState {
            it.copy(
                isLoading = false,
                errorState = errorState,
                presetItemsState = emptyList(),
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
        getAllPresets()
    }

    override fun onClickPreset(presetId: Int) {
        sendNewEffect(PresetsUiEffect.NavigateToItemsListScreen(presetId))
    }

    override fun showErrorScreen() {
        updateState { it.copy(isLoading = false, showErrorScreen = true) }
    }
}