package presentation.screen.order.items

import androidx.compose.runtime.Immutable
import domain.entity.Item
import presentation.base.ErrorState
import presentation.screen.order.modifiers.ItemModifierState

@Immutable
data class ItemsListState(
    val errorMessage: String = "",
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val itemsState: List<ItemState> = emptyList(),
)

@Immutable
data class ItemState(
    val id: Int = 0,
    val name: String = "",
)


fun Item.toItemState(): ItemState = ItemState(
    id = id,
    name = name,
    //if (AppLanguage.code.value == LanguageCode.EN.value) name else name2
)


fun ItemState.toItemModifierState(): ItemModifierState = ItemModifierState(
    id, name
)