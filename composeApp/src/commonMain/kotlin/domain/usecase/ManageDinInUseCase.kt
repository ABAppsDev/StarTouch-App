package domain.usecase

import domain.entity.AssignDrawer
import domain.gateway.IDinInGateway

class ManageDinInUseCase(
    private val dinInGateway: IDinInGateway,
) {
    suspend fun getAllOnlineUsers(outletId: Int, restId: Int): List<AssignDrawer> {
        return dinInGateway.getAllOnlineUsers(outletId, restId)
    }
}