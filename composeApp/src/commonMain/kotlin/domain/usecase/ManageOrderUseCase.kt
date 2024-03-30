package domain.usecase

import domain.entity.Item
import domain.entity.Preset
import domain.gateway.IOrderGateway

class ManageOrderUseCase(
    private val orderGateway: IOrderGateway
) {
    suspend fun getAllPresets(
        outletID: Int, restID: Int,
        checkId: Long
    ): List<Preset> {
        return orderGateway.getAllPresets(outletID, restID, checkId)
    }

    suspend fun getAllItems(
        outletID: Int,
        restID: Int,
        presetID: Int,
        checkId: Long,
        priceLvlId: Int
    ): List<Item> {
        return orderGateway.getAllItems(outletID, restID, presetID, checkId, priceLvlId)
    }

    suspend fun checkItemHasModifiers(
        restID: Int,
        itemID: Int,
        priceLvlId: Int
    ): List<Item> {
        return orderGateway.checkItemHasModifiers(restID, itemID, priceLvlId)
    }

    suspend fun checkItemHasChildren(
        restID: Int,
        itemID: Int,
        priceLvlId: Int
    ): List<Item> {
        return orderGateway.checkItemHasChildren(restID, itemID, priceLvlId)
    }
}