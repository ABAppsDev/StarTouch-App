package domain.gateway

import domain.entity.Item
import domain.entity.Preset

interface IOrderGateway {
    suspend fun getAllPresets(
        outletID: Int,
        restID: Int,
        checkId: Long
    ): List<Preset>

    suspend fun getAllItems(
        outletID: Int,
        restID: Int,
        presetID: Int,
        checkId: Long,
        priceLvlId: Int
    ): List<Item>

    suspend fun checkItemHasModifiers(
        restID: Int,
        itemID: Int,
        priceLvlId: Int
    ): List<Item>

    suspend fun checkItemHasChildren(
        restID: Int,
        itemID: Int,
        priceLvlId: Int
    ): List<Item>
}