package presentation.screen.order

import androidx.compose.runtime.Immutable
import data.util.AppLanguage
import domain.entity.Item
import domain.entity.Preset
import presentation.base.ErrorState
import util.LanguageCode

@Immutable
data class OrderState(
    val errorMessage: String = "",
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val showErrorScreen: Boolean = false,
    val isPresetVisible: Boolean = false,
    val presetItemsState: List<PresetItemState> = emptyList(),
    val itemModifiersState: List<ItemModifierState> = emptyList(),
    val itemChildrenState: List<ItemState> = emptyList(),
    val itemsState: List<ItemState> = emptyList(),
)

@Immutable
data class PresetItemState(
    val id: Int = 0,
    val name: String = "",
)


fun Preset.toPresetItemState(): PresetItemState = PresetItemState(
    id = id,
    name = if (AppLanguage.code.value == LanguageCode.EN.value) name else name2,
)

@Immutable
data class ItemModifierState(
    val id: Int = 0,
    val name: String = "",
    val price: Float = 0.0f,
)

@Immutable
data class ItemState(
    val id: Int = 0,
    val name: String = "",
    val price: Float = 0.0f,
)


fun Item.toItemModifierState(): ItemModifierState = ItemModifierState(
    id = id,
    name = if (AppLanguage.code.value == LanguageCode.EN.value) name else name2,
    price = staticPrice
)

fun Item.toItemState(): ItemState = ItemState(
    id = id,
    name = if (AppLanguage.code.value == LanguageCode.EN.value) name else name2,
    price = staticPrice
)

fun ItemState.toItemModifierState(): ItemModifierState = ItemModifierState(
    id = id,
    name = name,
    price = price
)
