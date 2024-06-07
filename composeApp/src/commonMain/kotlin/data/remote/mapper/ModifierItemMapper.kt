package data.remote.mapper

import data.remote.model.ModifierItemDto
import domain.entity.ModifierItem

fun ModifierItemDto.toEntity(): ModifierItem = ModifierItem(
    code = this.code ?: "",
    name = this.name ?: "",
    name2 = this.name2 ?: "",
    modCount = this.modCount ?: 0,
    taxable = this.taxable ?: false,
    assimbly = this.assimbly ?: false,
    noServiceCharge = this.noServiceCharge ?: false,
    isModifier = this.isModifier ?: false,
    standAlone = this.standAlone ?: false,
    printOnChick = this.printOnChick ?: false,
    printOnReport = this.printOnReport ?: false,
    printItemOnCheck = this.printItemOnCheck ?: false,
    followItem = this.followItem ?: false,
    openPrice = this.openPrice ?: false,
    staticPrice = this.staticPrice ?: 0f,
    openFood = this.openFood ?: false,
    modPrice = this.modPrice ?: 0f,
    modPrice0 = this.modPrice0 ?: false,
    useItemTimer = this.useItemTimer ?: false,
    itemTimerValue = this.itemTimerValue ?: 0,
    itemID = this.itemID ?: 0,
    modifierGroupName = this.modifierGroupName ?: "",
    modifierGroupName2 = this.modifierGroupName2 ?: "",
    multiPick = this.multiPick ?: false,
    maxPick = this.maxPick ?: 0,
    allowNoPick = this.allowNoPick ?: false,
    modifierGroupID = this.modifierGroupID ?: 0,
    prePaidCard = this.prePaidCard ?: false,
    pickFollowItemQty = this.pickFollowItemQty ?: false,
    multiChooseForSameMod = this.multiChooseForSameMod ?: false
)