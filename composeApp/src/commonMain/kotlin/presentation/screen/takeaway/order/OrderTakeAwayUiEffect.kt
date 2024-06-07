package presentation.screen.takeaway.order

sealed interface OrderTakeAwayUiEffect {
    data object NavigateBackToTakeAway : OrderTakeAwayUiEffect
    data object NavigateBackToHome : OrderTakeAwayUiEffect
}