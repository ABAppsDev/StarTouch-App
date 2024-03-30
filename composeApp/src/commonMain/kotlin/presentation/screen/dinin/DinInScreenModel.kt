package presentation.screen.dinin

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.StarTouchSetup
import domain.entity.OpenNewCheck
import domain.entity.TableData
import domain.usecase.GetRestaurantTablesUseCase
import domain.usecase.ManageChecksUseCase
import domain.usecase.ManageDinInUseCase
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class DinInScreenModel(
    private val getRestaurantTablesUseCase: GetRestaurantTablesUseCase,
    private val manageDinInUseCase: ManageDinInUseCase,
    private val manageChecksUseCase: ManageChecksUseCase
) : BaseScreenModel<DinInState, DinInUiEffect>(DinInState()), DinInInteractionListener {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        getTables()
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
                getRestaurantTablesUseCase(
                    StarTouchSetup.OUTLET_ID,
                    StarTouchSetup.REST_ID
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
                    is ErrorState.ValidationNetworkError -> errorState.message.toString()
                    else -> "Unknown error"
                }
            )
        }
    }

    fun retry() {
        getTables()
    }

    override fun onClickOk() {
        if (state.value.dinInDialogueState.coversCount.isEmpty())
            onError(errorState = ErrorState.ValidationError("Enter covers number"))
        else if (state.value.dinInDialogueState.coversCount.toInt() <= 0)
            onError(errorState = ErrorState.ValidationError("Zero covers not allowed"))
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
                    manageChecksUseCase.openNewCheck(
                        OpenNewCheck(
                            tableId = value.tableId,
                            tableName = value.tableName.toString(),
                            serverId = value.dinInDialogueState.serverId,
                            workStationId = StarTouchSetup.WORK_STATION_ID,
                            outletId = StarTouchSetup.OUTLET_ID,
                            restId = StarTouchSetup.REST_ID,
                            covers = value.dinInDialogueState.coversCount.toInt(),
                            userId = StarTouchSetup.USER_ID,
                            dateTime = StarTouchSetup.SYSTEM_DATE
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
                            )
                        }
                        sendNewEffect(DinInUiEffect.NavigateToOrderScreen(check.id))
                    }
                },
                onError = ::onError
            )
        }
    }

    override fun onClickTable(tableId: Int, tableName: Int) {
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
                    StarTouchSetup.REST_ID
                )
            },
            onSuccess = { assignDrawers ->
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
                            is ErrorState.ValidationNetworkError -> errorState.message.toString()
                            else -> "Unknown error"
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
                    is ErrorState.UnknownError -> errorState.message.toString()
                    is ErrorState.EmptyData -> errorState.message.toString()
                    is ErrorState.ValidationError -> errorState.message.toString()
                    is ErrorState.ValidationNetworkError -> errorState.message.toString()
                    else -> "Unknown error"
                }
            )
        }
    }

    override fun onClickAssignDrawer(id: Int) {
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

    override fun onDismissDinInDialogue() {
        updateState {
            it.copy(
                isLoading = false,
                dinInDialogueState = DinInDialogueState(),
                tableId = 0,
                tableName = 0,
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

    override fun showWarningDialogue(tableId: Int, tableName: Int) {
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
                tableName = 0,
                tableId = 0
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
                dinInDialogueState = it.dinInDialogueState.copy(
                    isVisible = true,
                    isLoading = true,
                    isSuccess = false,
                    isLoadingButton = false,
                    serverId = 0
                )
            )
        }
        onClickTable(state.value.tableId, state.value.tableName)
    }
}