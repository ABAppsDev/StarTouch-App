package presentation.screen.order.modifiers

import androidx.compose.runtime.Immutable
import domain.entity.Item
import presentation.base.ErrorState

@Immutable
data class ItemModifiersListState(
    val errorMessage: String = "",
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val itemModifiersState: List<ItemModifierState> = emptyList(),
)

@Immutable
data class ItemModifierState(
    val id: Int = 0,
    val name: String = "",
)


fun Item.toItemModifierState(): ItemModifierState = ItemModifierState(
    id = id,
    name = name,
    //if (AppLanguage.code.value == LanguageCode.EN.value) name else name2
)