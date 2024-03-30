package presentation.screen.dinin

sealed interface DinInUiEffect {
    data class NavigateToOrderScreen(val checkId: Long) : DinInUiEffect
}