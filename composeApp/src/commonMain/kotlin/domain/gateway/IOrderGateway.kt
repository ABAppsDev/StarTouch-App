package domain.gateway

import domain.entity.Item
import domain.entity.ModifierItem
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
    ): List<Item>

    suspend fun checkItemHasModifiers(
        restID: Int,
        itemID: Int,
        outletID: Int,
    ): List<ModifierItem>

    suspend fun checkItemHasChildren(
        outletID: Int,
        itemID: Int,
    ): List<Item>
}