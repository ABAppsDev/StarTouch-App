package presentation.screen.home

interface HomeInteractionListener {
    fun onClickLogOn()
    fun onClickLogOff()
    fun onClickLogin()
    fun onClickLogout()
    fun onClickDinIn()
    fun onClickTakeAway()
    fun onClickEnterPasscode()
    fun onClickExitApp()
    fun onClickSettings()
    fun onClickSettingsOk()
    fun onUserNameChanged(username: String)
    fun onPasswordChanged(password: String)
    fun onPassCodeChanged(passcode: String)
    fun onDismissAttendanceDialogue()
    fun onDismissPermissionDialogue()
    fun showErrorDialogue()
    fun showWarningDialogue()
    fun onDismissErrorDialogue()
    fun onDismissWarningDialogue()
    fun onDismissSettingsDialogue()
}