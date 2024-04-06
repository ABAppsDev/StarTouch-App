package presentation.screen.dinin

import domain.entity.FireItems

sealed interface DinInUiEffect {
    data class NavigateToOrderScreen(
        val checkId: Long,
        val items: List<FireItems>,
        val isReopened: Boolean
    ) : DinInUiEffect

    data object NavigateBackToHome : DinInUiEffect
}