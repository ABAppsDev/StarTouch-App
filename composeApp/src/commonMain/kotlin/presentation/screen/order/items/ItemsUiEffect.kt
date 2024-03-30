package presentation.screen.order.items

import presentation.screen.order.modifiers.ItemModifierState

sealed interface ItemsUiEffect {
    data class NavigateToItemsModifierListScreen(val items: List<ItemModifierState>) : ItemsUiEffect

    data object NavigateBackToPresetsListScreen : ItemsUiEffect
}