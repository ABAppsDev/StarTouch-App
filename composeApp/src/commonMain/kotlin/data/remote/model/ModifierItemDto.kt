package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ModifierItemDto(
    val code: String? = null,
    val name: String? = null,
    val name2: String? = null,
    val modCount: Int? = null,
    val taxable: Boolean? = null,
    val assimbly: Boolean? = null,
    val noServiceCharge: Boolean? = null,
    val isModifier: Boolean? = null,
    val standAlone: Boolean? = null,
    val printOnChick: Boolean? = null,
    val printOnReport: Boolean? = null,
    val printItemOnCheck: Boolean? = null,
    val followItem: Boolean? = null,
    val openPrice: Boolean? = null,
    val staticPrice: Float? = null,
    val openFood: Boolean? = null,
    val modPrice: Float? = null,
    val modPrice0: Boolean? = null,
    val useItemTimer: Boolean? = null,
    val itemTimerValue: Int? = null,
    val itemID: Int? = null,
    val modifierGroupName: String? = null,
    val modifierGroupName2: String? = null,
    val multiPick: Boolean? = null,
    val maxPick: Int? = null,
    val allowNoPick: Boolean? = null,
    val modifierGroupID: Int? = null,
    val prePaidCard: Boolean? = null,
    val pickFollowItemQty: Boolean? = null,
    val multiChooseForSameMod: Boolean? = null,
)