package presentation.screen.dinin

import domain.entity.FireItems

sealed interface DinInUiEffect {
    data class NavigateToOrderScreen(
        val checkId: Long,
        val items: List<FireItems>,
        val isReopened: Boolean,
        val checkNumber: Int,
    ) : DinInUiEffect

    data object NavigateBackToHome : DinInUiEffect
}