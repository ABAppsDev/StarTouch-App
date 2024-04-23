package presentation.screen.dinin

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.AppLanguage
import data.util.StarTouchSetup
import domain.entity.OpenNewCheck
import domain.entity.TableData
import domain.usecase.GetAppSetupUseCase
import domain.usecase.ManageChecksUseCase
import domain.usecase.ManageDinInUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class DinInScreenModel(
    private val manageDinInUseCase: ManageDinInUseCase,
    private val manageChecksUseCase: ManageChecksUseCase,
    private val setupUseCase: GetAppSetupUseCase,
) : BaseScreenModel<DinInState, DinInUiEffect>(DinInState()), DinInInteractionListener {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        getTables()
        getAllRooms()
        //socketTables()
    }

    private fun socketTables() {
        viewModelScope.launch(Dispatchers.Default) {
            if (state.value.roomId != 0)
                while (true) {
                    if (state.value.roomId != 0)
                        try {
                            delay(8000)
                            if (state.value.roomId != 0) {
                                val tables = manageDinInUseCase.getTablesDataByRoomId(
                                    StarTouchSetup.OUTLET_ID,
                                    StarTouchSetup.REST_ID,
                                    state.value.roomId
                                )
                                updateState {
                                    it.copy(
                                        tablesDetails = tables.map { table ->
                                            table.toTableDetailsState()
                                        },
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            updateState {
                                it.copy(
                                    errorDinInState = ErrorState.UnknownError(""),
                                    errorMessage = ""
                                )
                            }
                        }
                }
        }
    }


    private fun getTables() {
        updateState {
            it.copy(
                isLoading = true,
                errorDinInState = null,
                errorMessage = "",
            )
        }
        tryToExecute(
            function = {
                manageDinInUseCase.getTablesDataByRoomId(
                    StarTouchSetup.OUTLET_ID,
                    StarTouchSetup.REST_ID,
                    StarTouchSetup.MAIN_ROOM_ID
                )
            },
            onSuccess = ::onGetTablesSuccess,
            onError = ::onGetTablesError
        )
    }

    private fun onGetTablesSuccess(tables: List<TableData>) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorDinInState = null,
                showErrorScreen = false,
                tablesDetails = tables.map { table ->
                    table.toTableDetailsState()
                }
            )
        }
    }

    private fun onGetTablesError(errorState: ErrorState) {
        updateState {
            it.copy(
                isLoading = false,
                errorDinInState = errorState,
                tablesDetails = emptyList(),
                errorMessage = when (errorState) {
                    is ErrorState.NetworkError -> errorState.message.toString()
                    is ErrorState.NotFound -> errorState.message.toString()
                    is ErrorState.ServerError -> errorState.message.toString()
                    is ErrorState.UnknownError -> errorState.message.toString()
                    is ErrorState.EmptyData -> errorState.message.toString()
                    is ErrorState.PermissionDenied -> errorState.message.toString()
                    is ErrorState.ValidationNetworkError -> errorState.message.toString()
                    else -> "Unknown error"
                }
            )
        }
    }

    fun retry() {
        getTables()
        getAllRooms()
        updateState { it.copy(roomId = StarTouchSetup.MAIN_ROOM_ID) }
    }

    override fun onClickOk() {
        if (state.value.dinInDialogueState.coversCount.isEmpty())
            onError(errorState = ErrorState.ValidationError("Enter covers number"))
        else {
            updateState {
                it.copy(
                    isLoading = false,
                    errorMessage = "",
                    errorDinInState = null,
                    dinInDialogueState = it.dinInDialogueState.copy(
                        isLoadingButton = true,
                        isLoading = false,
                    )
                )
            }
            tryToExecute(
                function = {
                    val value = state.value
                    if (!state.value.validation) manageChecksUseCase.openNewCheck(
                        OpenNewCheck(
                            tableId = value.tableId,
                            tableName = value.tableName,
                            serverId = value.dinInDialogueState.serverId,
                            workStationId = StarTouchSetup.WORK_STATION_ID,
                            outletId = StarTouchSetup.OUTLET_ID,
                            restId = StarTouchSetup.REST_ID,
                            covers = value.dinInDialogueState.coversCount.toInt(),
                            userId = StarTouchSetup.USER_ID,
                            dateTime = StarTouchSetup.SYSTEM_DATE
                        )
                    )
                    else manageChecksUseCase.openNewCheckWithChecksOpen(
                        OpenNewCheck(
                            tableId = value.tableId,
                            tableName = value.tableName,
                            serverId = value.dinInDialogueState.serverId,
                            workStationId = StarTouchSetup.WORK_STATION_ID,
                            outletId = StarTouchSetup.OUTLET_ID,
                            restId = StarTouchSetup.REST_ID,
                            covers = value.dinInDialogueState.coversCount.toInt(),
                            userId = StarTouchSetup.USER_ID,
                            dateTime = StarTouchSetup.SYSTEM_DATE,
                            checkId = value.tablesDetails.find { it.tableId == value.tableId }?.checkId
                        )
                    )
                },
                onSuccess = { check ->
                    launchDelayed(500) {
                        updateState {
                            it.copy(
                                isLoading = false,
                                errorMessage = "",
                                errorDinInState = null,
                                dinInDialogueState = DinInDialogueState(),
                                validation = false
                            )
                        }
                        sendNewEffect(
                            DinInUiEffect.NavigateToOrderScreen(
                                check.id,
                                emptyList(),
                                false,
                                check.serial
                            )
                        )
                    }
                },
                onError = { errorState ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorState = errorState,
                            showErrorScreen = false,
                            errorDinInState = null,
                            dinInDialogueState = it.dinInDialogueState.copy(
                                isLoading = false,
                                isLoadingButton = false,
                            ),
                            errorDialogueIsVisible = true,
                            errorMessage = when (errorState) {
                                is ErrorState.NetworkError -> errorState.message.toString()
                                is ErrorState.NotFound -> errorState.message.toString()
                                is ErrorState.ServerError -> errorState.message.toString()
                                is ErrorState.PermissionDenied -> errorState.message.toString()
                                is ErrorState.UnknownError -> errorState.message.toString()
                                is ErrorState.EmptyData -> errorState.message.toString()
                                is ErrorState.ValidationError -> errorState.message.toString()
                                is ErrorState.ValidationNetworkError -> errorState.message.toString()
                                else -> "Unknown error"
                            }
                        )
                    }
                }
            )
        }
    }

    override fun onClickTable(tableId: Int, tableName: String) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorDinInState = null,
                dinInDialogueState = it.dinInDialogueState.copy(
                    isVisible = true,
                    isLoading = true,
                    isLoadingButton = false,
                    isSuccess = false
                ),
                tableId = tableId,
                tableName = tableName
            )
        }
        tryToExecute(
            function = {
                manageDinInUseCase.getAllOnlineUsers(
                    StarTouchSetup.OUTLET_ID,
                    StarTouchSetup.REST_ID,
                    StarTouchSetup.USER_ID
                )
            },
            onSuccess = { assignDrawers ->
                if (assignDrawers.isEmpty()) {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = "",
                            errorDinInState = null,
                            dinInDialogueState = it.dinInDialogueState.copy(
                                isVisible = true,
                                isLoading = false,
                                isLoadingButton = false,
                                isSuccess = true,
                                serverId = StarTouchSetup.USER_ID,
                                assignDrawers = emptyList()
                            ),
                        )
                    }
                    StarTouchSetup.SERVER_ID = StarTouchSetup.USER_ID
                } else {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = "",
                            errorDinInState = null,
                            dinInDialogueState = it.dinInDialogueState.copy(
                                isVisible = true,
                                isLoading = false,
                                isLoadingButton = false,
                                assignDrawers = assignDrawers.map { assignDrawer ->
                                    assignDrawer.toAssignDrawerState()
                                }
                            ),
                        )
                    }
                }
            },
            onError = { errorState ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorDinInState = errorState,
                        dinInDialogueState = DinInDialogueState(),
                        errorDialogueIsVisible = true,
                        errorMessage = when (errorState) {
                            is ErrorState.NetworkError -> errorState.message.toString()
                            is ErrorState.NotFound -> errorState.message.toString()
                            is ErrorState.ServerError -> errorState.message.toString()
                            is ErrorState.UnknownError -> errorState.message.toString()
                            is ErrorState.PermissionDenied -> errorState.message.toString()
                            is ErrorState.EmptyData -> errorState.message.toString()
                            is ErrorState.ValidationError -> errorState.message.toString()
                            is ErrorState.ValidationNetworkError -> errorState.message.toString()
                            else -> "Logon Error"
                        }
                    )
                }
            }
        )
    }

    private fun onError(errorState: ErrorState) {
        updateState {
            it.copy(
                isLoading = false,
                errorDinInState = errorState,
                dinInDialogueState = it.dinInDialogueState.copy(
                    isLoading = false,
                    isLoadingButton = false,
                ),
                errorDialogueIsVisible = true,
                errorMessage = when (errorState) {
                    is ErrorState.NetworkError -> errorState.message.toString()
                    is ErrorState.NotFound -> errorState.message.toString()
                    is ErrorState.ServerError -> errorState.message.toString()
                    is ErrorState.PermissionDenied -> errorState.message.toString()
                    is ErrorState.UnknownError -> errorState.message.toString()
                    is ErrorState.EmptyData -> errorState.message.toString()
                    is ErrorState.ValidationError -> errorState.message.toString()
                    is ErrorState.ValidationNetworkError -> errorState.message.toString()
                    else -> "Logon Error"
                }
            )
        }
    }

    override fun onClickAssignCheck(id: Int) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorDinInState = null,
                dinInDialogueState = it.dinInDialogueState.copy(
                    isVisible = true,
                    isLoading = false,
                    isLoadingButton = false,
                    isSuccess = true,
                    serverId = id,
                    assignDrawers = emptyList()
                ),
            )
        }
        StarTouchSetup.SERVER_ID = id
    }

    override fun onClickCheck(id: Long, serial: Int) {
        tryToExecute(
            function = {
                manageChecksUseCase.reOpenCheck(id)
            },
            onSuccess = { items ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "",
                        errorDinInState = null,
                        dinInDialogueState = it.dinInDialogueState.copy(
                            isVisible = false,
                            isLoading = false,
                            isLoadingButton = false,
                            isSuccess = false,
                            assignDrawers = emptyList(),
                            checks = emptyList()
                        ),
                        checkId = id
                    )
                }
                sendNewEffect(DinInUiEffect.NavigateToOrderScreen(id, items, true, serial))
            },
            onError = ::onError
        )
    }

    override fun onCoversCountChanged(covers: String) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorDinInState = null,
                dinInDialogueState = it.dinInDialogueState.copy(
                    coversCount = covers
                )
            )
        }
    }

    override fun onTableNameChanged(tableName: String) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorDinInState = null,
                dinInDialogueState = it.dinInDialogueState.copy(
                    tableName = tableName
                )
            )
        }
    }

    override fun onDismissDinInDialogue() {
        updateState {
            it.copy(
                isLoading = false,
                dinInDialogueState = DinInDialogueState(),
                tableId = 0,
                tableName = "0",
                validation = false
            )
        }
    }

    override fun showErrorDialogue() {
        updateState {
            it.copy(
                isLoading = false,
                errorDialogueIsVisible = true
            )
        }
    }

    override fun onDismissErrorDialogue() {
        updateState {
            it.copy(
                isLoading = false,
                errorDialogueIsVisible = false
            )
        }
    }

    override fun showErrorScreen() {
        updateState { it.copy(isLoading = false, showErrorScreen = true) }
    }

    override fun showWarningDialogue(tableId: Int, tableName: String) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorDinInState = null,
                warningDialogueIsVisible = true,
                tableName = tableName,
                tableId = tableId
            )
        }
    }

    override fun onDismissWarningDialogue() {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorDinInState = null,
                warningDialogueIsVisible = false,
                tableName = "0",
                tableId = 0,
                validation = false
            )
        }
    }

    override fun onConfirmButtonClick() {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorDinInState = null,
                warningDialogueIsVisible = false,
                validation = true,
                dinInDialogueState = it.dinInDialogueState.copy(
                    isVisible = true,
                    isLoading = false,
                    isSuccess = false,
                    isLoadingButton = false,
                    serverId = 0,
                    isNamedTable = true
                )
            )
        }
    }

    override fun onLongClick(tableId: Long) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorDinInState = null,
                dinInDialogueState = it.dinInDialogueState.copy(
                    isVisible = true,
                    isLoading = true,
                    isLoadingButton = false,
                    isSuccess = false
                ),
                tableId = tableId.toInt(),
            )
        }
        if (state.value.roomId != 0) {
            tryToExecute(
                function = {
                    manageChecksUseCase.getAllChecksByTableId(
                        tableId = tableId.toInt(),
                        StarTouchSetup.OUTLET_ID,
                        StarTouchSetup.REST_ID,
                        serverId = StarTouchSetup.USER_ID,
                        userId = StarTouchSetup.USER_ID,
                    )
                },
                onSuccess = { checks ->
                    if (checks.size == 1) onClickCheck(checks[0].id, checks[0].checkSerial)
                    else {
                        updateState {
                            it.copy(
                                isLoading = false,
                                errorMessage = "",
                                errorDinInState = null,
                                dinInDialogueState = it.dinInDialogueState.copy(
                                    isVisible = true,
                                    isLoading = false,
                                    isLoadingButton = false,
                                ),
                            )
                        }
                        if (checks.isEmpty()) throw Exception("This Table Open By Another Waiter")
                        else updateState {
                            it.copy(
                                dinInDialogueState = it.dinInDialogueState.copy(checks = checks.map { check ->
                                    AssignCheckState(
                                        id = check.id,
                                        name = check.checkSerial.toString(),
                                        status = check.myStatus,
                                        tableName = check.myTable,
                                        date = check.myDateTime + " " + check.createDate
                                    )
                                }
                                )
                            )
                        }
                    }
                },
                onError = { errorState ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorDinInState = errorState,
                            dinInDialogueState = DinInDialogueState(),
                            errorDialogueIsVisible = true,
                            errorMessage = when (errorState) {
                                is ErrorState.NetworkError -> errorState.message.toString()
                                is ErrorState.NotFound -> errorState.message.toString()
                                is ErrorState.ServerError -> errorState.message.toString()
                                is ErrorState.UnknownError -> errorState.message.toString()
                                is ErrorState.EmptyData -> errorState.message.toString()
                                is ErrorState.ValidationError -> errorState.message.toString()
                                is ErrorState.PermissionDenied -> errorState.message.toString()
                                is ErrorState.ValidationNetworkError -> errorState.message.toString()
                                else -> "Logon Error"
                            }
                        )
                    }
                }
            )
        } else {
            tryToExecute(
                function = {
                    manageChecksUseCase.reOpenCheck(tableId)
                },
                onSuccess = { items ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = "",
                            errorDinInState = null,
                            dinInDialogueState = it.dinInDialogueState.copy(
                                isVisible = false,
                                isLoading = false,
                                isLoadingButton = false,
                                isSuccess = false,
                                assignDrawers = emptyList(),
                                checks = emptyList()
                            ),
                            checkId = tableId,
                        )
                    }
                    sendNewEffect(
                        DinInUiEffect.NavigateToOrderScreen(
                            tableId,
                            items,
                            true,
                            tableId.toString().takeLast(5).toInt()
                        )
                    )
                },
                onError = ::onError
            )
        }
    }

    override fun onClickBack() {
        viewModelScope.launch(Dispatchers.IO) {
            AppLanguage.code.emit(StarTouchSetup.DEFAULT_LANGUAGE)
            sendNewEffect(DinInUiEffect.NavigateBackToHome)
        }
    }

    override fun onClickTableGuest() {
        updateState {
            it.copy(
                isLoading = true,
                errorDinInState = null,
                errorMessage = "",
            )
        }
        tryToExecute(
            function = {
                manageDinInUseCase.getAllTablesGuest(
                    StarTouchSetup.OUTLET_ID,
                    StarTouchSetup.REST_ID
                )
            },
            onSuccess = { tables ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "",
                        errorDinInState = null,
                        showErrorScreen = false,
                        tablesDetails = tables.map { table ->
                            table.toTableDetailsState()
                        },
                        roomId = 0
                    )
                }
            },
            onError = { errorState ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorDinInState = errorState,
                        tablesDetails = emptyList(),
                        errorMessage = when (errorState) {
                            is ErrorState.NetworkError -> errorState.message.toString()
                            is ErrorState.NotFound -> errorState.message.toString()
                            is ErrorState.ServerError -> errorState.message.toString()
                            is ErrorState.UnknownError -> errorState.message.toString()
                            is ErrorState.EmptyData -> errorState.message.toString()
                            is ErrorState.PermissionDenied -> errorState.message.toString()
                            is ErrorState.ValidationNetworkError -> errorState.message.toString()
                            else -> "Logon Error"
                        }
                    )
                }
            }
        )
    }

    override fun onCreateTableGuest() {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorDinInState = null,
                dinInDialogueState = it.dinInDialogueState.copy(
                    isVisible = true,
                    isLoading = false,
                    isNamedTable = true
                ),
                tableId = 0,
            )
        }
    }

    override fun onClickRoom(id: Int) {
        updateState {
            it.copy(
                isLoading = true,
                errorDinInState = null,
                errorMessage = "",
            )
        }
        tryToExecute(
            function = {
                manageDinInUseCase.getTablesDataByRoomId(
                    StarTouchSetup.OUTLET_ID,
                    StarTouchSetup.REST_ID,
                    id
                )
            },
            onSuccess = { tables ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "",
                        errorDinInState = null,
                        showErrorScreen = false,
                        tablesDetails = tables.map { table ->
                            table.toTableDetailsState()
                        },
                        roomId = id
                    )
                }
            },
            onError = { errorState ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorDinInState = errorState,
                        tablesDetails = emptyList(),
                        errorMessage = when (errorState) {
                            is ErrorState.NetworkError -> errorState.message.toString()
                            is ErrorState.NotFound -> errorState.message.toString()
                            is ErrorState.ServerError -> errorState.message.toString()
                            is ErrorState.UnknownError -> errorState.message.toString()
                            is ErrorState.EmptyData -> errorState.message.toString()
                            is ErrorState.PermissionDenied -> errorState.message.toString()
                            is ErrorState.ValidationNetworkError -> errorState.message.toString()
                            else -> "Unknown error"
                        }
                    )
                }
            }
        )
    }

    override fun onEnterTableName() {
        if (state.value.dinInDialogueState.tableName.isEmpty())
            onError(errorState = ErrorState.ValidationError("Enter table name"))
        else {
            updateState {
                it.copy(
                    isLoading = false,
                    errorMessage = "",
                    errorDinInState = null,
                    dinInDialogueState = it.dinInDialogueState.copy(
                        isLoadingButton = true,
                        isLoading = false,
                        isNamedTable = false,
                    ),
                    tableName = state.value.dinInDialogueState.tableName
                )
            }
            onClickTable(state.value.tableId, state.value.dinInDialogueState.tableName)
        }
    }

    private fun getAllRooms() {
        updateState {
            it.copy(
                isLoading = true,
                errorDinInState = null,
                errorMessage = "",
            )
        }
        tryToExecute(
            function = {
                setupUseCase.getAllRoomsByOutletAndRestId(
                    StarTouchSetup.OUTLET_ID,
                    StarTouchSetup.REST_ID
                )
            },
            onSuccess = { rooms ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorDinInState = null,
                        errorMessage = "",
                        rooms = rooms.map { room ->
                            room.toState()
                        }.sortedByDescending { roomSorted ->
                            roomSorted.id == StarTouchSetup.MAIN_ROOM_ID
                        }
                    )
                }
            },
            onError = { errorState ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorDinInState = errorState,
                        dinInDialogueState = DinInDialogueState(),
                        errorDialogueIsVisible = true,
                        errorMessage = when (errorState) {
                            is ErrorState.NetworkError -> errorState.message.toString()
                            is ErrorState.NotFound -> errorState.message.toString()
                            is ErrorState.ServerError -> errorState.message.toString()
                            is ErrorState.PermissionDenied -> errorState.message.toString()
                            is ErrorState.UnknownError -> errorState.message.toString()
                            is ErrorState.EmptyData -> errorState.message.toString()
                            is ErrorState.ValidationError -> errorState.message.toString()
                            is ErrorState.ValidationNetworkError -> errorState.message.toString()
                            else -> "Unknown error"
                        }
                    )
                }
            }
        )
    }
}