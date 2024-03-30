package domain.usecase

import domain.entity.OpenCheck
import domain.entity.OpenNewCheck
import domain.gateway.IChecksGateway

class ManageChecksUseCase(
    private val checksGateway: IChecksGateway,
) {
    suspend fun openNewCheck(openNewCheck: OpenNewCheck): OpenCheck {
        return checksGateway.openNewCheck(openNewCheck)
    }

    suspend fun addItemsToCheck(
        checkID: Long,
        userID: String,
        //items: List<FireItems>,
    ) {
        //   checksGateway.addItemsToCheck(checkID, userID, items)
    }
}