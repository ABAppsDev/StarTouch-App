package presentation.screen.order

import androidx.compose.runtime.Immutable
import data.util.AppLanguage
import data.util.StarTouchSetup
import domain.entity.FireItems
import domain.entity.Item
import domain.entity.ModifierItem
import domain.entity.Preset
import presentation.base.ErrorState
import util.LanguageCode
import util.roundToDecimals
import kotlin.random.Random

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
    val warningItemIsVisible: Boolean = false,
    val itemId: Int = 0,
    val isLoadingButton: Boolean = false,
    val qty: Float = 0f,
    val showEnterOpenPrice: Boolean = false,
    val price: String = "",
    val deleted: Boolean = false,
    val tax: Float = 0f,
    val adj: Float = 0f,
    val amount: Float = 0f,
)

@Immutable
data class ModifyLastItemDialogue(
    val isVisible: Boolean = false,
    val comment: String = "",
    val itemId: Int = 0,
    val serial: Int = 0,
)

@Immutable
data class OrderItemState(
    val serial: Int = Random.nextInt(3),
    val id: Int = 0,
    val qty: Float = 0f,
    val name: String = "",
    val unitPrice: Float = 0f,
    val totalPrice: Float = unitPrice * qty,
    val counter: Int = 0,
    val fired: Boolean = false,
    val voided: Boolean = false,
    val pOnCheck: Boolean = false,
    val status: String = "",
    val isModifier: Boolean = false,
    val refModItem: Int = 0,
    val taxable: Boolean = false,
    val noServiceCharge: Boolean = false,
    val pOnReport: Boolean = false,
    val modifierGroupID: Int = 0,
    val pickFollowItemQty: Boolean = false,
    val modifierPick: Int = 0,
    val prePaidCard: Boolean = false,
    val refItemId: Int = 0,
    val adj: Float = run {
        var temp = 0f
        StarTouchSetup.adjustments.filter { f -> f.isDinIn }
            .forEach { adj ->
                if (adj.type == "Percentage")
                    temp =
                        totalPrice * (adj.value / 100)
                else if (adj.type == "Flat amount")
                    temp =
                        totalPrice + adj.value
            }
        temp.roundToDecimals(2)
    },
    val tax: Float = run {
        var temp = 0f
        StarTouchSetup.taxes.filter { f -> f.isDinIn }
            .forEach { tax ->
                if (tax.type == "Percentage")
                    temp =
                        (adj + totalPrice) * (tax.value / 100)
                else if (tax.type == "Flat amount")
                    temp =
                        (adj + totalPrice) + tax.value
            }
        temp.roundToDecimals(2)
    }
)

fun OrderItemState.toEntity(): FireItems = FireItems(
    id = id,
    price = unitPrice,
    qty = qty,
    name = name,
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
    refModItem = refModItem,
    voided = voided,
    modifierPick = modifierPick,
    status = status,
    serial = counter,
)

fun FireItems.toState(): OrderItemState = OrderItemState(
    id = id,
    name = name ?: "",
    unitPrice = price,
    serial = Random.nextInt(),
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
    refModItem = refModItem ?: 0,
    voided = voided ?: false,
    modifierPick = modifierPick ?: 0,
    status = status ?: "preparing",
    counter = serial ?: 0,
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
    val openPrice: Boolean = false,
)


fun ModifierItem.toItemModifierState(): ItemModifierState = ItemModifierState(
    id = itemID,
    name = if (AppLanguage.code.value == LanguageCode.EN.value) name else name2,
    price = staticPrice,
    isModifier = isModifier,
    taxable = taxable,
    prePaidCard = prePaidCard,
    pickFollowItemQty = pickFollowItemQty,
    modifierGroupID = modifierGroupID,
    noServiceCharge = noServiceCharge,
    pOnReport = printOnReport,
    pOnCheck = printOnChick,
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
    pOnCheck = printItemOnCheck,
    openPrice = openPrice,
)

fun ItemState.toItemModifierState(): ItemModifierState = ItemModifierState(
    id = id,
    name = name,
    price = price,
    pOnCheck = pOnCheck
)