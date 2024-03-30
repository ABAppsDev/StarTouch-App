package presentation.screen.order.modifiers

sealed interface ItemModifiersUiEffect {
    data object NavigateBackToPresetsListScreen : ItemModifiersUiEffect
}