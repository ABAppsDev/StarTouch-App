package presentation.screen.home

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.AppLanguage
import data.util.StarTouchSetup
import domain.entity.Account
import domain.entity.AppSetup
import domain.entity.Attendance
import domain.entity.UserApp
import domain.usecase.AdminSystemUseCase
import domain.usecase.ControlPermissionUseCase
import domain.usecase.GetAppSetupUseCase
import domain.usecase.ManageAttendanceUseCase
import domain.usecase.ManageSettingUseCase
import domain.usecase.ValidationAuthUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import presentation.base.BaseScreenModel
import presentation.base.ErrorState
import presentation.screen.home.util.AttendanceType
import presentation.screen.home.util.PermissionType
import resource.strings.IStringResources
import util.LanguageCode
import util.LocalizationManager
import util.getLanguageCodeByName

class HomeScreenModel(
    private val manageAuth: ManageAttendanceUseCase,
    private val controlPermission: ControlPermissionUseCase,
    private val getAppSetup: GetAppSetupUseCase,
    private val manageSetting: ManageSettingUseCase,
    private val validationAuth: ValidationAuthUseCase,
    private val adminSystemUseCase: AdminSystemUseCase,
) : BaseScreenModel<HomeState, HomeUiEffect>(HomeState()), HomeInteractionListener {

    override val viewModelScope: CoroutineScope get() = screenModelScope
    private lateinit var localMessage: IStringResources

    init {
        initHomeDetails()
    }

    private fun initHomeDetails() {
        updateState {
            it.copy(
                isLoading = true,
                errorHomeDetailsState = null,
                errorMessage = "",
            )
        }
        tryToExecute(
            function = {
                getDataFromCache()
                getAppSetup(StarTouchSetup.OUTLET_ID, StarTouchSetup.REST_ID)
            },
            onSuccess = ::onInitHomeDetailsSuccess,
            onError = ::onInitHomeDetailsError
        )
    }

    fun retry() {
        initHomeDetails()
    }

    private suspend fun getDataFromCache() {
        val restId = manageSetting.getRestId()
        val outletId = manageSetting.getOutletId()
        val tableId = manageSetting.getDinInMainRoomId()
        val wsd = manageSetting.getWorkStationId()
        val apiUrl = manageSetting.getApiUrl().trim()
        if (restId != 0 && outletId != 0 && wsd != 0 && apiUrl != "") {
            StarTouchSetup.REST_ID = restId
            StarTouchSetup.OUTLET_ID = outletId
            StarTouchSetup.WORK_STATION_ID = tableId
            StarTouchSetup.MAIN_ROOM_ID = wsd
        } else throw Exception("Please setup app first then try again")
    }

    private fun onInitHomeDetailsSuccess(appSetup: AppSetup) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "",
                errorHomeDetailsState = null,
                homeDetailsState = appSetup.toHomeDetailsState()
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            AppLanguage.code.emit(state.value.homeDetailsState.appLanguage.value)
            StarTouchSetup.DEFAULT_LANGUAGE = state.value.homeDetailsState.appLanguage.value
            StarTouchSetup.SYSTEM_DATE = appSetup.systemDate
            localMessage = LocalizationManager.getStringResources(
                LanguageCode.entries.find { code ->
                    code.value == AppLanguage.code.value
                } ?: LanguageCode.EN
            )
        }
    }

    private fun onInitHomeDetailsError(errorState: ErrorState) {
        updateState {
            it.copy(
                isLoading = false,
                errorHomeDetailsState = errorState,
                errorMessage = when (errorState) {
                    is ErrorState.NetworkError -> errorState.message.toString()
                    is ErrorState.NotFound -> errorState.message.toString()
                    is ErrorState.ServerError -> errorState.message.toString()
                    is ErrorState.UnknownError -> errorState.message.toString()
                    else -> "Unknown error"
                }
            )
        }
    }

    private fun login(userName: String, password: String) {
        tryToExecute(
            function = {
                validationAuth.validateAttendance(
                    username = userName,
                    password = password
                )
                manageAuth.login(
                    Account(userName, password, StarTouchSetup.OUTLET_ID, StarTouchSetup.REST_ID)
                )
            },
            onSuccess = (::onLoginSuccess),
            onError = (::onFailed)
        )
    }

    private fun logout(userName: String, password: String) {
        tryToExecute(
            function = {
                validationAuth.validateAttendance(
                    username = userName,
                    password = password
                )
                manageAuth.logout(
                    Account(userName, password, StarTouchSetup.OUTLET_ID, StarTouchSetup.REST_ID)
                )
            },
            onSuccess = (::onLogoutSuccess),
            onError = (::onFailed)
        )
    }

    private fun onLoginSuccess(user: Attendance) {
        updateState {
            it.copy(
                errorMessage = "",
                errorState = null,
                attendanceDialogueState = it.attendanceDialogueState.copy(
                    isLoading = false,
                    isVisible = false
                )
            )
        }
        launchDelayed(500) {
            updateState {
                it.copy(
                    attendanceDialogueState = it.attendanceDialogueState.copy(
                        message = "${localMessage.welcome}, ${user.name}",
                    )
                )
            }
        }
        launchDelayed(1500) {
            updateState {
                it.copy(
                    attendanceDialogueState = it.attendanceDialogueState.copy(
                        message = "",
                    )
                )
            }
        }
    }

    private fun onLogoutSuccess(user: Attendance) {
        updateState {
            it.copy(
                errorMessage = "",
                isLoading = false,
                errorState = null,
                attendanceDialogueState = it.attendanceDialogueState.copy(
                    isLoading = false,
                    isVisible = false
                )
            )
        }
        launchDelayed(500) {
            updateState {
                it.copy(
                    attendanceDialogueState = it.attendanceDialogueState.copy(
                        message = "${localMessage.bye}, ${user.name}",
                    )
                )
            }
        }
        launchDelayed(1500) {
            updateState {
                it.copy(
                    errorMessage = "",
                    errorState = null,
                    attendanceDialogueState = it.attendanceDialogueState.copy(
                        message = "",
                    )
                )
            }
        }
    }

    private fun checkDinIn(passcode: String) {
        tryToExecute(
            function = {
                validationAuth.validatePermissionPasscode(
                    passcode = passcode,
                )
                controlPermission.checkDinInPermission(
                    passcode = passcode,
                    restID = StarTouchSetup.REST_ID,
                    outletID = StarTouchSetup.OUTLET_ID,
                )
            },
            onSuccess = (::onDinInSuccess),
            onError = (::onFailed)
        )
    }

    override fun onDismissPermissionDialogue() {
        updateState {
            it.copy(
                permissionDialogueState = it.permissionDialogueState.copy(
                    isLoading = false,
                    isVisible = false
                )
            )
        }
    }

    override fun showErrorDialogue() {
        updateState { it.copy(errorDialogueIsVisible = true, warningDialogueIsVisible = false) }
    }

    override fun showWarningDialogue() {
        updateState { it.copy(warningDialogueIsVisible = true) }
    }

    override fun onDismissErrorDialogue() {
        updateState {
            it.copy(
                errorDialogueIsVisible = false,
                warningDialogueIsVisible = false,
                errorState = null,
            )
        }
    }

    override fun onDismissWarningDialogue() {
        updateState {
            it.copy(
                warningDialogueIsVisible = false,
                errorState = null,
            )
        }
    }

    override fun onDismissSettingsDialogue() {
        updateState {
            it.copy(
                errorMessage = "",
                isLoading = false,
                errorState = null,
                settingsDialogueState = it.settingsDialogueState.copy(
                    isVisible = false,
                    isLoading = false
                )
            )
        }
    }

    private fun onDinInSuccess(userApp: UserApp) {
        updateState {
            it.copy(
                errorMessage = "",
                errorState = null,
                permissionDialogueState = it.permissionDialogueState.copy(
                    isLoading = false,
                    isVisible = false
                )
            )
        }
        launchDelayed(500) {
            viewModelScope.launch(Dispatchers.IO) {
                userApp.apply {
                    StarTouchSetup.TOKEN = token
                    StarTouchSetup.USER_ID = id
                    StarTouchSetup.USER_LANGUAGE = getLanguageCodeByName(userApp.language).value
                }
                AppLanguage.code.emit(StarTouchSetup.USER_LANGUAGE)
                updateState {
                    it.copy(
                        errorMessage = "",
                        errorState = null,
                        attendanceDialogueState = AttendanceDialogueState(
                            message = "",
                        )
                    )
                }
                sendNewEffect(HomeUiEffect.NavigateToDinInScreen)
            }
        }
    }


    private fun onFailed(errorState: ErrorState) {
        updateState {
            it.copy(
                errorState = errorState,
                attendanceDialogueState = it.attendanceDialogueState.copy(
                    isLoading = false,
                ),
                permissionDialogueState = it.permissionDialogueState.copy(
                    isLoading = false
                ),
                errorMessage = when (errorState) {
                    is ErrorState.UnknownError -> errorState.message.toString()
                    is ErrorState.ServerError -> errorState.message.toString()
                    is ErrorState.UnAuthorized -> errorState.message.toString()
                    is ErrorState.NotFound -> errorState.message.toString()
                    is ErrorState.ValidationNetworkError -> errorState.message.toString()
                    is ErrorState.NetworkError -> errorState.message.toString()
                    is ErrorState.ValidationError -> errorState.message.toString()
                    else -> "Something wrong happened please try again !"
                }
            )
        }
    }

    override fun onClickLogOn() {
        updateState {
            it.copy(
                attendanceDialogueState = AttendanceDialogueState(
                    isVisible = true,
                    attendanceType = AttendanceType.LOGIN.name
                )
            )
        }
    }

    override fun onClickLogOff() {
        updateState {
            it.copy(
                attendanceDialogueState = AttendanceDialogueState(
                    isVisible = true,
                    attendanceType = AttendanceType.LOGOUT.name
                )
            )
        }
    }

    override fun onClickLogin() {
        updateState {
            it.copy(
                errorState = null,
                errorMessage = "",
                attendanceDialogueState = it.attendanceDialogueState.copy(
                    isLoading = true,
                )
            )
        }
        login(
            state.value.attendanceDialogueState.username,
            state.value.attendanceDialogueState.password
        )
    }

    override fun onClickLogout() {
        updateState {
            it.copy(
                errorState = null,
                errorMessage = "",
                attendanceDialogueState = it.attendanceDialogueState.copy(
                    isLoading = true,
                )
            )
        }
        logout(
            state.value.attendanceDialogueState.username,
            state.value.attendanceDialogueState.password
        )
    }

    override fun onClickDinIn() {
        updateState {
            it.copy(
                permissionDialogueState = PermissionDialogueState(
                    isVisible = true,
                    permissionType = PermissionType.DIN_IN.name
                )
            )
        }
    }

    override fun onClickEnterPasscode() {
        updateState {
            it.copy(
                errorState = null,
                errorMessage = "",
                permissionDialogueState = it.permissionDialogueState.copy(isLoading = true)
            )
        }
        if (state.value.permissionDialogueState.permissionType == PermissionType.DIN_IN.name)
            checkDinIn(state.value.permissionDialogueState.passcode)
        else {
            updateState {
                it.copy(
                    errorState = null,
                    errorMessage = "",
                    permissionDialogueState = it.permissionDialogueState.copy(isLoading = false)
                )
            }
            showWarningDialogue()
        }
    }

    override fun onClickExitApp() {
        updateState {
            it.copy(
                permissionDialogueState = PermissionDialogueState(
                    isVisible = true,
                    permissionType = PermissionType.EXIT.name
                )
            )
        }
    }

    override fun onClickSettings() {
        updateState {
            it.copy(
                settingsDialogueState = SettingsDialogueState(
                    isVisible = true
                )
            )
        }
    }

    override fun onClickSettingsOk() {
        updateState {
            it.copy(
                errorState = null,
                errorMessage = "",
                settingsDialogueState = it.settingsDialogueState.copy(isLoading = true)
            )
        }
        if (adminSystemUseCase.checkPermissionWithPassword(state.value.settingsDialogueState.password)) {
            updateState {
                it.copy(
                    settingsDialogueState = it.settingsDialogueState.copy(
                        isLoading = false,
                        isVisible = false
                    )
                )
            }
            launchDelayed(1000) {
                updateState {
                    it.copy(
                        settingsDialogueState = SettingsDialogueState()
                    )
                }
            }
            sendNewEffect(HomeUiEffect.NavigateToSetting)
        } else updateState {
            it.copy(
                errorMessage = "Logon Error",
                errorState = ErrorState.PermissionDenied("Logon Error"),
                settingsDialogueState = it.settingsDialogueState.copy(isLoading = false),
            )
        }
    }

    fun exitApp(passcode: String) {
        updateState {
            it.copy(
                errorState = null,
                errorMessage = "",
                permissionDialogueState = it.permissionDialogueState.copy(isLoading = true),
                warningDialogueIsVisible = false
            )
        }
        if (passcode.isNotEmpty() && adminSystemUseCase.checkPermissionWithPasscode(passcode.toInt())) {
            onExitAppSuccess(true)
        } else {
            tryToExecute(
                function = {
                    validationAuth.validatePermissionPasscode(passcode)
                    controlPermission.checkExitAppPermission(
                        StarTouchSetup.OUTLET_ID,
                        StarTouchSetup.REST_ID,
                        passcode
                    )
                },
                onSuccess = ::onExitAppSuccess,
                onError = ::onFailed
            )
        }
    }

    private fun onExitAppSuccess(isSuccess: Boolean) {
        updateState {
            it.copy(
                errorMessage = "",
                errorState = null,
                permissionDialogueState = PermissionDialogueState(),
                exitApp = isSuccess
            )
        }
    }

    override fun onUserNameChanged(username: String) {
        updateState {
            it.copy(
                attendanceDialogueState = it.attendanceDialogueState.copy(
                    username = username,
                    usernameError = ErrorWrapper()
                ),
                settingsDialogueState = it.settingsDialogueState.copy(
                    username = username
                )
            )
        }
    }

    override fun onPasswordChanged(password: String) {
        updateState {
            it.copy(
                attendanceDialogueState = it.attendanceDialogueState.copy(
                    password = password,
                    passwordError = ErrorWrapper()
                ),
                settingsDialogueState = it.settingsDialogueState.copy(
                    password = password
                )
            )
        }
    }

    override fun onPassCodeChanged(passcode: String) {
        updateState {
            it.copy(
                permissionDialogueState = it.permissionDialogueState.copy(
                    passcode = passcode,
                    passcodeError = ErrorWrapper()
                )
            )
        }
    }

    override fun onDismissAttendanceDialogue() {
        updateState {
            it.copy(
                errorMessage = "",
                isLoading = false,
                errorState = null,
                attendanceDialogueState = it.attendanceDialogueState.copy(
                    isLoading = false,
                    isVisible = false
                )
            )
        }
    }
}