package domain.usecase

import data.remote.model.ReOpenCheck
import domain.entity.FireItems
import domain.entity.OpenCheck
import domain.entity.OpenNewCheck
import domain.gateway.IChecksGateway

class ManageChecksUseCase(
    private val checksGateway: IChecksGateway,
) {
    suspend fun openNewCheck(openNewCheck: OpenNewCheck): OpenCheck {
        return checksGateway.openNewCheck(openNewCheck)
    }

    suspend fun makeCheckAborted(checkId: Long, outletID: Int, restID: Int) {
        checksGateway.makeCheckAborted(checkId, outletID, restID)
    }

    suspend fun reOpenCheck(checkId: Long): List<FireItems> = checksGateway.reOpenCheck(checkId)

    suspend fun addItemsToExistCheck(
        checkID: Long,
        serverId: Int,
        userID: String,
        items: List<FireItems>
    ): Boolean {
        return checksGateway.addItemsToExistCheck(checkID, serverId, userID, items)
    }

    suspend fun getAllChecksByTableId(
        tableId: Int,
        outletID: Int,
        restID: Int,
        serverId: Int,
        userId: Int
    ): List<ReOpenCheck> {
        return checksGateway.getAllChecksByTableId(tableId, outletID, restID, serverId, userId)
    }

    suspend fun addItemsToCheck(
        checkID: Long,
        serverId: Int,
        userID: String,
        items: List<FireItems>,
    ): Boolean {
        return checksGateway.fireItems(checkID, serverId, userID, items)
    }
}