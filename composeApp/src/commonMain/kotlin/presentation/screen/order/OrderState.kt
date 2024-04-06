package presentation.screen.order

import androidx.compose.runtime.Immutable
import data.util.AppLanguage
import data.util.StarTouchSetup
import domain.entity.FireItems
import domain.entity.Item
import domain.entity.Preset
import presentation.base.ErrorState
import util.LanguageCode

val orders = mutableListOf<OrderItemState>()

@Immutable
data class OrderState(
    val errorMessage: String = "",
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val isLoadingOrder: Boolean = false,
    val showErrorScreen: Boolean = false,
    val isPresetVisible: Boolean = false,
    val presetItemsState: List<PresetItemState> = emptyList(),
    val itemModifiersState: List<ItemModifierState> = emptyList(),
    val itemChildrenState: List<ItemState> = emptyList(),
    val itemsState: List<ItemState> = emptyList(),
    val selectedPresetId: Int = 0,
    val selectedItemId: Int = 0,
    val isRefresh: Boolean = false,
    val isFinishOrder: Boolean = false,
    val modifyLastItemDialogue: ModifyLastItemDialogue = ModifyLastItemDialogue(),
    val orderItemState: List<OrderItemState> = orders.toList(),
    val warningDialogueIsVisible: Boolean = false,
)

@Immutable
data class ModifyLastItemDialogue(
    val isVisible: Boolean = false,
    val comment: String = "",
)

@Immutable
data class OrderItemState(
    val id: Int = 0,
    val name: String = "",
    val qty: Int = 0,
    val unitPrice: Float = 0f,
    val totalPrice: Float = unitPrice * qty,
    val noServiceCharge: Boolean = false,
    val isModifier: Boolean = false,
    val taxable: Boolean = false,
    val pickFollowItemQty: Boolean = false,
    val modifierGroupID: Int = 0,
    val prePaidCard: Boolean = false,
    val pOnReport: Boolean = false,
    val fired: Boolean = false,
    val pOnCheck: Boolean = false,
    val refModItem: Int = 0,
)

fun OrderItemState.toEntity(): FireItems = FireItems(
    id = id,
    price = unitPrice,
    qty = qty,
    totalPrice = totalPrice,
    ws = StarTouchSetup.WORK_STATION_ID,
    isModifier = isModifier,
    modifierGroupID = modifierGroupID,
    noServiceCharge = noServiceCharge,
    pickFollowItemQty = pickFollowItemQty,
    prePaidCard = prePaidCard,
    taxable = taxable,
    pOnReport = pOnReport,
    fired = fired,
    pOnCheck = pOnCheck,
    refModItem = refModItem
)

fun FireItems.toState(): OrderItemState = OrderItemState(
    id = id,
    name = name ?: "",
    unitPrice = price,
    qty = qty,
    totalPrice = totalPrice,
    isModifier = isModifier,
    modifierGroupID = modifierGroupID,
    noServiceCharge = noServiceCharge,
    pickFollowItemQty = pickFollowItemQty,
    prePaidCard = prePaidCard,
    taxable = taxable,
    pOnCheck = pOnCheck ?: false,
    pOnReport = pOnReport ?: false,
    fired = fired ?: false,
    refModItem = refModItem?:0
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
    val noServiceCharge: Boolean = false,
    val isModifier: Boolean = false,
    val taxable: Boolean = false,
    val pickFollowItemQty: Boolean = false,
    val modifierGroupID: Int = 0,
    val prePaidCard: Boolean = false,
    val pOnReport: Boolean = false,
    val pOnCheck: Boolean = false,
    val refModItem: Int = 0,
)

@Immutable
data class ItemState(
    val id: Int = 0,
    val name: String = "",
    val price: Float = 0.0f,
    val noServiceCharge: Boolean = false,
    val isModifier: Boolean = false,
    val taxable: Boolean = false,
    val pickFollowItemQty: Boolean = false,
    val modifierGroupID: Int = 0,
    val prePaidCard: Boolean = false,
    val pOnReport: Boolean = false,
    val pOnCheck: Boolean = false,
)


fun Item.toItemModifierState(): ItemModifierState = ItemModifierState(
    id = id,
    name = if (AppLanguage.code.value == LanguageCode.EN.value) name else name2,
    price = staticPrice,
    isModifier = isModifier,
    taxable = taxable,
    prePaidCard = prePaidCard,
    pickFollowItemQty = false,
    modifierGroupID = 0,
    noServiceCharge = noServiceCharge,
    pOnReport = printOnReport,
    pOnCheck = printItemOnCheck
)

fun Item.toItemState(): ItemState = ItemState(
    id = id,
    name = if (AppLanguage.code.value == LanguageCode.EN.value) name else name2,
    price = staticPrice,
    isModifier = isModifier,
    taxable = taxable,
    prePaidCard = prePaidCard,
    pickFollowItemQty = false,
    modifierGroupID = 0,
    noServiceCharge = noServiceCharge,
    pOnReport = printOnReport,
    pOnCheck = printItemOnCheck
)

fun ItemState.toItemModifierState(): ItemModifierState = ItemModifierState(
    id = id,
    name = name,
    price = price,
    pOnCheck = pOnCheck
)