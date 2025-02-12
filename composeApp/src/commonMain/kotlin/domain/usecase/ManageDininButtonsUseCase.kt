package domain.usecase

import domain.entity.FireItems
import domain.gateway.IDinInButtonGateway

class ManageDininButtonsUseCase(
    private val dininButtonsGateway: IDinInButtonGateway
) {
    suspend fun getTableItems(
        checkId: Long,
        tableId: Long
    ): List<FireItems> {
        return dininButtonsGateway.getTableItems(checkId, tableId)
    }

    suspend fun moveItems(
        fromCheckId: Long,
        toCheckId: Long,
        itemSerials: List<Int>,
    ) {
        return dininButtonsGateway.moveItems(fromCheckId, toCheckId, itemSerials)
    }


}