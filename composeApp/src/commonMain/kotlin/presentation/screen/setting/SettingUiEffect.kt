package presentation.screen.setting

sealed interface SettingUiEffect {
    data object NavigateBackToHome : SettingUiEffect
}