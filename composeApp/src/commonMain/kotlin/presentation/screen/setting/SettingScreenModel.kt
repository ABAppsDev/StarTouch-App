package presentation.screen.setting

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.StarTouchSetup
import domain.usecase.GetAppSetupUseCase
import domain.usecase.ManageSettingUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class SettingScreenModel(
    private val manageSettings: ManageSettingUseCase,
    private val getAppSetupUseCase: GetAppSetupUseCase,
) : BaseScreenModel<SettingState, SettingUiEffect>(SettingState()),
    SettingInteractionListener {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        initValues()
    }

    private fun initValues() {
        viewModelScope.launch(Dispatchers.IO) {
            val wsId =
                if (StarTouchSetup.WORK_STATION_ID != 0) StarTouchSetup.WORK_STATION_ID.toString()
                else manageSettings.getWorkStationId().toString()
            val isCallCenter = manageSettings.getIsCallCenter()
            val isQuickSaleLoopBack = manageSettings.getIsBackToHome()
            val apiUrl = manageSettings.getApiUrl().trim()
            updateState {
                it.copy(
                    workStationId = wsId,
                    apiUrl = apiUrl,
                    isCallCenter = isCallCenter,
                    isQuickSaleLoopBack = isQuickSaleLoopBack,
                )
            }
            if (apiUrl != "") {
                launch(Dispatchers.IO) {
                    val restId = if (StarTouchSetup.REST_ID != 0) StarTouchSetup.REST_ID
                    else manageSettings.getRestId()
                    val outletId = if (StarTouchSetup.OUTLET_ID != 0) StarTouchSetup.OUTLET_ID
                    else manageSettings.getOutletId()
                    val roomId = if (StarTouchSetup.MAIN_ROOM_ID != 0) StarTouchSetup.MAIN_ROOM_ID
                    else manageSettings.getDinInMainRoomId()
                    if (restId != 0) getRestaurantFromCache(restId) else getAllRestaurants()
                    if (outletId != 0) getOutletFromCache(outletId)
                    if (roomId != 0) getMainRoomFromCache(roomId)
                }
            } else {
                updateState {
                    it.copy(
                        errorState = ErrorState.ServerError(""),
                        errorMessage = "Please enter the ip address"
                    )
                }
            }
        }
    }

    private suspend fun getRestaurantFromCache(restId: Int) {
        tryToExecuteWithoutScope(
            function = {
                getAppSetupUseCase.getAllRestaurants().map { it.toSetupItemState() }
            },
            onSuccess = { restaurants ->
                updateState {
                    it.copy(
                        restaurants = restaurants,
                        selectedRestaurant = (restaurants.find { restaurant ->
                            restaurant.id == restId
                        } ?: SetupItemState(0, "")).also { rest ->
                            StarTouchSetup.REST_ID = rest.id
                        }
                    )
                }
            },
            onError = ::onError
        )
    }

    private suspend fun getOutletFromCache(outletId: Int) {
        tryToExecuteWithoutScope(
            function = {
                getAppSetupUseCase.getAllOutletsByRestId(state.value.selectedRestaurant.id)
                    .map { it.toSetupItemState() }
            },
            onSuccess = { outlets ->
                updateState {
                    it.copy(
                        outlets = outlets,
                        selectedOutlet = (outlets.find { outlet ->
                            outlet.id == outletId
                        } ?: SetupItemState(0, "")).also { outletCache ->
                            StarTouchSetup.OUTLET_ID = outletCache.id
                        }
                    )
                }
            },
            onError = ::onError
        )
    }

    private suspend fun getMainRoomFromCache(roomId: Int) {
        tryToExecuteWithoutScope(
            function = {
                getAppSetupUseCase.getAllRoomsByOutletAndRestId(
                    state.value.selectedOutlet.id,
                    state.value.selectedRestaurant.id
                ).map { it.toSetupItemState() }
            },
            onSuccess = { rooms ->
                updateState {
                    it.copy(
                        rooms = rooms,
                        selectedMainRoom = (rooms.find { room ->
                            room.id == roomId
                        } ?: SetupItemState(0, "")).also { roomCache ->
                            StarTouchSetup.MAIN_ROOM_ID = roomCache.id
                        }
                    )
                }
            },
            onError = ::onError
        )
    }

    fun retry() {
        initValues()
    }

    private fun getAllRestaurants() {
        updateState { it.copy(isLoading = true, errorState = null, errorMessage = "") }
        tryToExecute(
            function = {
                getAppSetupUseCase.getAllRestaurants()
            },
            onSuccess = { restaurants ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorState = null,
                        errorMessage = "",
                        restaurants = restaurants.map { restaurant ->
                            restaurant.toSetupItemState()
                        }
                    )
                }
            },
            onError = ::onError
        )
    }

    private fun getAllOutlets(id: Int) {
        updateState { it.copy(isLoading = true, errorState = null, errorMessage = "") }
        tryToExecute(
            function = {
                getAppSetupUseCase.getAllOutletsByRestId(id)
            },
            onSuccess = { outlets ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorState = null,
                        errorMessage = "",
                        outlets = outlets.map { outlet ->
                            outlet.toSetupItemState()
                        }
                    )
                }
            },
            onError = ::onError
        )
    }

    private fun getAllRooms(id: Int) {
        updateState { it.copy(isLoading = true, errorState = null, errorMessage = "") }
        tryToExecute(
            function = {
                getAppSetupUseCase.getAllRoomsByOutletAndRestId(
                    id, state.value.selectedRestaurant.id
                )
            },
            onSuccess = { rooms ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorState = null,
                        errorMessage = "",
                        rooms = rooms.map { room ->
                            room.toSetupItemState()
                        }
                    )
                }
            },
            onError = ::onError
        )
    }

    override fun onClickSave() {
        updateState { it.copy(isLoading = true, isSuccess = false) }
        tryToExecute(
            function = {
                val valueState = state.value
                if (state.value.errorState == null) {
                    manageSettings.saveOutletId(valueState.selectedOutlet.id)
                    manageSettings.saveRestId(valueState.selectedRestaurant.id)
                    manageSettings.saveWorkStationId(valueState.workStationId)
                    manageSettings.saveDinInMainRoomId(valueState.selectedMainRoom.id)
                    manageSettings.saveIsBackToHome(valueState.isQuickSaleLoopBack)
                    manageSettings.saveIsCallCenter(valueState.isCallCenter)
                }
                manageSettings.saveApiUrl(valueState.apiUrl)
                StarTouchSetup.REST_ID = state.value.selectedRestaurant.id
                StarTouchSetup.OUTLET_ID = state.value.selectedOutlet.id
                StarTouchSetup.MAIN_ROOM_ID = state.value.selectedMainRoom.id
                if (state.value.workStationId.trim() != "")
                    StarTouchSetup.WORK_STATION_ID = state.value.workStationId.toInt()
            },
            onSuccess = {
                updateState { it.copy(isLoading = false, isSuccess = true) }
            },
            onError = ::onError,
        )
    }

    private fun onError(error: ErrorState) {
        updateState {
            it.copy(
                errorState = error,
                isLoading = false,
                errorMessage = when (error) {
                    is ErrorState.UnknownError -> error.message.toString()
                    is ErrorState.ServerError -> error.message.toString()
                    is ErrorState.NotFound -> error.message.toString()
                    is ErrorState.ValidationNetworkError -> error.message.toString()
                    is ErrorState.NetworkError -> error.message.toString()
                    else -> "Something wrong happened please try again !"
                }
            )
        }
    }

    override fun onClickClose() {
        updateState { it.copy(isSuccess = false) }
        sendNewEffect(SettingUiEffect.NavigateBackToHome)
    }

    override fun onChooseRest(id: Int) {
        updateState { it.copy(selectedRestaurant = it.selectedRestaurant.copy(id = id)) }
        if (state.value.restaurants.isNotEmpty()) getAllOutlets(id)
    }

    override fun onChooseOutlet(id: Int) {
        updateState { it.copy(selectedOutlet = it.selectedOutlet.copy(id = id)) }
        if (state.value.outlets.isNotEmpty()) getAllRooms(id)
    }

    override fun onChooseDinInRoom(id: Int) {
        updateState { it.copy(selectedMainRoom = it.selectedMainRoom.copy(id = id)) }
    }

    override fun onApiUrlChanged(apiUrl: String) {
        updateState { it.copy(apiUrl = apiUrl) }
    }

    override fun onWorkStationIdChanged(wsId: String) {
        updateState { it.copy(workStationId = wsId) }
    }

    override fun onSelectedCallCenter(isSelected: Boolean) {
        updateState { it.copy(isCallCenter = isSelected) }
    }

    override fun onQuickLoopBackSelected(isSelected: Boolean) {
        updateState { it.copy(isQuickSaleLoopBack = isSelected) }
    }
}