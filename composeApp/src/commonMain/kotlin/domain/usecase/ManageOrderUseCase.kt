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
    ): List<Item> {
        return orderGateway.getAllItems(outletID, restID, presetID, checkId)
    }

    suspend fun checkItemHasModifiers(
        restID: Int,
        itemID: Int,
        outletID: Int,
    ): List<Item> {
        return orderGateway.checkItemHasModifiers(restID, itemID,outletID)
    }

    suspend fun checkItemHasChildren(
        outletID: Int,
        itemID: Int,
    ): List<Item> {
        return orderGateway.checkItemHasChildren(outletID, itemID)
    }
}