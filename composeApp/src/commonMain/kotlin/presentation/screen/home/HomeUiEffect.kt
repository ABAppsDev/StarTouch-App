package presentation.screen.home

sealed interface HomeUiEffect {
    data object NavigateToDinInScreen : HomeUiEffect
    data object NavigateToOrderScreen : HomeUiEffect
    data object NavigateToTakeAwayScreen : HomeUiEffect
    data object NavigateToSetting : HomeUiEffect
}