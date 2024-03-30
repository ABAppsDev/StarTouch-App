package domain.usecase

import domain.entity.TableData
import domain.gateway.IDinInGateway

class GetRestaurantTablesUseCase(
    private val dinInGateway: IDinInGateway
) {
    suspend operator fun invoke(outletID: Int, restID: Int): List<TableData> {
        return dinInGateway.getTableData(outletID, restID)
    }
}