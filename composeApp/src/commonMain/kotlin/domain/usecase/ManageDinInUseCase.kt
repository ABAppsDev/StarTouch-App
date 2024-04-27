package domain.usecase

import domain.entity.AssignCheck
import domain.entity.TableData
import domain.gateway.IDinInGateway

class ManageDinInUseCase(
    private val dinInGateway: IDinInGateway,
) {
    suspend fun getAllOnlineUsers(outletId: Int, restId: Int, userID: Int): List<AssignCheck> {
        return dinInGateway.getAllOnlineUsers(outletId, restId, userID)
    }

    suspend fun getTablesDataByRoomId(outletID: Int, restID: Int, roomID: Int): List<TableData> =
        dinInGateway.getTablesDataByRoomId(outletID, restID, roomID)

    suspend fun getAllTablesGuest(outletId: Int, restId: Int) =
        dinInGateway.getAllTablesGuest(outletId, restId)

    suspend fun deleteTable() {
        dinInGateway.deleteTable()
    }
}