package domain.gateway

import domain.entity.AssignCheck
import domain.entity.TableData

interface IDinInGateway {
    suspend fun getTableData(outletID: Int, restID: Int): List<TableData>
    suspend fun getAllOnlineUsers(outletId: Int, restId: Int, userID: Int): List<AssignCheck>
    suspend fun getTablesDataByRoomId(outletID: Int, restID: Int, roomID: Int): List<TableData>
    suspend fun getAllTablesGuest(outletID: Int, restID: Int): List<TableData>
}