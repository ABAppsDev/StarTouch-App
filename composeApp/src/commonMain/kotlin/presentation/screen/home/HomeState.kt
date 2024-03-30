package presentation.screen.home

import androidx.compose.runtime.Immutable
import domain.entity.AppSetup
import presentation.base.ErrorState
import util.LanguageCode
import util.getLanguageCodeByName

@Immutable
data class HomeState(
    val errorMessage: String = "",
    val errorHomeDetailsState: ErrorState? = null,
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val homeDetailsState: HomeDetailsState = HomeDetailsState(),
    val attendanceDialogueState: AttendanceDialogueState = AttendanceDialogueState(),
    val settingsDialogueState: SettingsDialogueState = SettingsDialogueState(),
    val permissionDialogueState: PermissionDialogueState = PermissionDialogueState(),
    val errorDialogueIsVisible: Boolean = false,
    val warningDialogueIsVisible: Boolean = false,
    val exitApp: Boolean = false,
    val isRefresh:Boolean=false,
)

@Immutable
data class HomeDetailsState(
    val systemDate: String = "1/1/0001",
    val outletName: String = "ABApps",
    val appLanguage: LanguageCode = LanguageCode.EN,
)

@Immutable
data class AttendanceDialogueState(
    val isLoading: Boolean = false,
    val isVisible: Boolean = false,
    val username: String = "",
    val password: String = "",
    val usernameError: ErrorWrapper = ErrorWrapper(),
    val passwordError: ErrorWrapper = ErrorWrapper(),
    val attendanceType: String = "",
    val message: String = "",
)

@Immutable
data class SettingsDialogueState(
    val isLoading: Boolean = false,
    val isVisible: Boolean = false,
    val username: String = "",
    val password: String = "",
)

@Immutable
data class PermissionDialogueState(
    val isLoading: Boolean = false,
    val isVisible: Boolean = false,
    val passcode: String = "",
    val passcodeError: ErrorWrapper = ErrorWrapper(),
    val permissionType: String = "",
)

@Immutable
data class ErrorWrapper(
    val errorMessage: String = "",
    val isError: Boolean = false
)

fun AppSetup.toHomeDetailsState(): HomeDetailsState = HomeDetailsState(
    outletName = outletName,
    systemDate = systemDate.date.toString().replace("-", "/"),
    appLanguage = getLanguageCodeByName(defaultLanguage)
)