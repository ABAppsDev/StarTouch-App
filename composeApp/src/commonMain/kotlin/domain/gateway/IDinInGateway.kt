package domain.gateway

import domain.entity.AssignDrawer
import domain.entity.TableData

interface IDinInGateway {
    suspend fun getTableData(outletID: Int, restID: Int): List<TableData>
    suspend fun getAllOnlineUsers(outletId: Int, restId: Int): List<AssignDrawer>
}