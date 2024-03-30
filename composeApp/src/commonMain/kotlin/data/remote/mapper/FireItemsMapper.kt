package data.remote.mapper

import data.remote.model.FireItemsDto
import domain.entity.FireItems

fun FireItemsDto.toEntity() = FireItems(
    id = id ?: 0,
    price = price ?: 0f,
    qty = qty ?: 0,
    totalPrice = totalPrice ?: 0f,
    modifierGroupID = modifierGroupID ?: 0,
    isModifier = isModifier ?: false,
    noServiceCharge = noServiceCharge ?: false,
    ws = ws ?: 0,
    pickFollowItemQty = pickFollowItemQty ?: false,
    prePaidCard = prePaidCard ?: false,
    taxable = taxable ?: false
)


fun FireItems.toDto() =
    FireItemsDto(
        totalPrice,
        noServiceCharge,
        isModifier,
        taxable,
        pickFollowItemQty,
        modifierGroupID,
        prePaidCard,
        ws,
        id,
        price,
        qty
    )