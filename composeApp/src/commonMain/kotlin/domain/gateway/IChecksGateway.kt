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

    suspend fun getAllCheks(
        tableId: Int,
        outletID: Int,
        restID: Int,
        serverId: Int,
        userId: Int
    ): List<ReOpenCheck>
}