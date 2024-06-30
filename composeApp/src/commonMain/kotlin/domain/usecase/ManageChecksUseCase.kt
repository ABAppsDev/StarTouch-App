package domain.usecase

import data.remote.model.ReOpenCheck
import domain.entity.FireItems
import domain.entity.OpenCheck
import domain.entity.OpenNewCheck
import domain.gateway.IChecksGateway
import domain.gateway.IDinInGateway

class ManageChecksUseCase(
    private val checksGateway: IChecksGateway,
    private val dinInGateway: IDinInGateway,
) {
    suspend fun openNewCheck(openNewCheck: OpenNewCheck): OpenCheck {
        return checksGateway.openNewCheck(openNewCheck)
    }

    suspend fun deleteTable() {
        dinInGateway.deleteTable()
    }

    suspend fun openNewCheckTableGuest(openNewCheck: OpenNewCheck): OpenCheck {
        return checksGateway.openNewCheckTableGuest(openNewCheck)
    }

    suspend fun openNewCheckWithChecksOpen(openNewCheck: OpenNewCheck): OpenCheck {
        return checksGateway.openNewCheckWithChecksOpen(openNewCheck)
    }

    suspend fun makeCheckAborted(checkId: Long, outletID: Int, restID: Int) {
        checksGateway.makeCheckAborted(checkId, outletID, restID)
    }

    suspend fun reOpenCheck(checkId: Long, tableId: Int? = null): List<FireItems> =
        checksGateway.reOpenCheck(checkId, tableId)

    suspend fun addItemsToExistCheck(
        checkID: Long,
        serverId: Int,
        userID: String,
        items: List<FireItems>,
        taxes: Float,
        adjustments: Float,
    ): Boolean {
        return checksGateway.addItemsToExistCheck(
            checkID,
            serverId,
            userID,
            items,
            taxes,
            adjustments
        )
    }

    suspend fun getAllChecksByTableId(
        tableId: Int,
        checkId: Long,
        outletID: Int,
        restID: Int,
        serverId: Int,
        userId: Int
    ): List<ReOpenCheck> {
        return checksGateway.getAllChecksByTableId(
            tableId,
            checkId,
            outletID,
            restID,
            serverId,
            userId
        )
    }

    suspend fun addItemsToCheck(
        checkID: Long,
        serverId: Int,
        userID: String,
        items: List<FireItems>,
        taxes: Float,
        adjustments: Float,
    ): Boolean {
        return checksGateway.fireItems(checkID, serverId, userID, items, taxes, adjustments)
    }

    suspend fun deleteTable(checkId: Long) {
        checksGateway.deleteTable(checkId)
    }
}