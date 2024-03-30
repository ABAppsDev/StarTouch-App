package domain.usecase

import domain.entity.Item
import domain.entity.Preset
import domain.gateway.IOrderGateway

class ManageOrderUseCase(
    private val orderGateway: IOrderGateway
) {
    suspend fun getAllPresets(outletID: Int, restID: Int): List<Preset> {
        return orderGateway.getAllPresets(outletID, restID)
    }

    suspend fun getAllItems(outletID: Int, restID: Int, presetID: Int): List<Item> {
        return orderGateway.getAllItems(outletID, restID, presetID)
    }

    suspend fun checkItemHasModifiers(restID: Int, itemID: Int): List<Item> {
        return orderGateway.checkItemHasModifiers(restID, itemID)
    }
}