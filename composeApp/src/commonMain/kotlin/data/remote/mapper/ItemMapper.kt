package data.remote.mapper

import data.remote.model.ItemDto
import domain.entity.Item
import util.getDateNow

fun ItemDto.toEntity(): Item = Item(
    id = id ?: 0,
    code = code ?: "",
    name = name ?: "",
    name2 = name2 ?: "",
    barCode = barCode ?: "",
    crossCode = crossCode ?: 0,
    taxable = taxable ?: false,
    assembly = assembly ?: false,
    subCategoryId = subCategoryId ?: 0,
    isModifier = isModifier ?: false,
    standAlone = standAlone ?: false,
    printOnChick = printOnChick ?: false,
    printOnReport = printOnReport ?: false,
    followItem = followItem ?: false,
    imageItem = imageItem,
    cost = cost ?: 0f,
    openPrice = openPrice ?: false,
    staticPrice = staticPrice ?: 0f,
    description1 = description1 ?: "",
    description2 = description2 ?: "",
    description3 = description3 ?: "",
    description4 = description4 ?: "",
    modPrice0 = modPrice0 ?: false,
    useItemTimer = useItemTimer ?: false,
    itemTimerValue = itemTimerValue ?: 0,
    active = active ?: false,
    createDate = createDate ?: getDateNow(),
    modifiedDate = modifiedDate ?: getDateNow(),
    userId = userId ?: 0,
    noServiceCharge = noServiceCharge ?: false,
    marketPrice = marketPrice ?: false,
    itemTrack = itemTrack ?: false,
    printItemOnCheck = printItemOnCheck ?: false,
    isParent = isParent ?: false,
    restIdActive = restIdActive ?: 0,
    openFood = openFood ?: false,
    modPrice = modPrice ?: 0f,
    prePaidCard = prePaidCard ?: false,
    modifierCount = modifierCount ?: 0
)