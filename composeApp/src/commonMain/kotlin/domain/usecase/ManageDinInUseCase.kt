package domain.usecase

import domain.entity.AssignCheck
import domain.gateway.IDinInGateway

class ManageDinInUseCase(
    private val dinInGateway: IDinInGateway,
) {
    suspend fun getAllOnlineUsers(outletId: Int, restId: Int): List<AssignCheck> {
        return dinInGateway.getAllOnlineUsers(outletId, restId)
    }
}