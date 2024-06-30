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
        userID: String,
        items: List<FireItems>,
        taxes: Float,
        adjustments: Float,
    ): Boolean

    suspend fun openNewCheckWithChecksOpen(openNewCheck: OpenNewCheck): OpenCheck

    suspend fun getAllChecksByTableId(
        tableId: Int,
        checkId: Long,
        outletID: Int,
        restID: Int,
        serverId: Int,
        userId: Int
    ): List<ReOpenCheck>

    suspend fun makeCheckAborted(checkId: Long, outletID: Int, restID: Int)

    suspend fun reOpenCheck(checkId: Long, tableId: Int? = null): List<FireItems>
    suspend fun addItemsToExistCheck(
        checkID: Long,
        serverId: Int,
        userID: String,
        items: List<FireItems>,
        taxes: Float,
        adjustments: Float,
    ): Boolean

    suspend fun settle(
        checkID: Long,
        cashierId: Int,
        taxes: Float,
        adjustments: Float,
        amount: Float,
        outletID: Int,
        restID: Int,
        ws: Int,
    ): Boolean

    suspend fun openNewCheckTableGuest(openNewCheck: OpenNewCheck): OpenCheck
    suspend fun deleteTable(checkId: Long)
}