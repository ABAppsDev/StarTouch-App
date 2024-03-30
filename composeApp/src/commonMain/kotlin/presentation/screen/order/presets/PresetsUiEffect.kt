package presentation.screen.order.presets

sealed interface PresetsUiEffect {
    data class NavigateToItemsListScreen(val presetId: Int) : PresetsUiEffect
}