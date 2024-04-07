package domain.gateway

import data.remote.model.ReOpenCheck
import domain.entity.FireItems
import domain.entity.OpenCheck
import domain.entity.OpenNewCheck


interface IChecksGateway {
    suspend fun openNewCheck(openNewCheck: OpenNewCheck): OpenCheck
    suspend fun fireItems(
        checkID: Long,
        serverId: Int,
        userID: String, items: List<FireItems>
    ): Boolean

    suspend fun openNewCheckWithChecksOpen(openNewCheck: OpenNewCheck): OpenCheck

    suspend fun getAllChecksByTableId(
        tableId: Int,
        outletID: Int,
        restID: Int,
        serverId: Int,
        userId: Int
    ): List<ReOpenCheck>

    suspend fun makeCheckAborted(checkId: Long, outletID: Int, restID: Int)

    suspend fun reOpenCheck(checkId: Long): List<FireItems>
    suspend fun addItemsToExistCheck(
        checkID: Long,
        serverId: Int,
        userID: String,
        items: List<FireItems>
    ): Boolean
}