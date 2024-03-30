package presentation.screen.order

sealed interface OrderUiEffect {
    data object NavigateBackToDinIn : OrderUiEffect
    data object NavigateBackToHome : OrderUiEffect
}