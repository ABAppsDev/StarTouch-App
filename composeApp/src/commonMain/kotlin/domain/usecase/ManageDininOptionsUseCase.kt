package domain.usecase

import domain.gateway.IDinInOptionsGateway

class ManageDininOptionsUseCase(
    private val dinInOptionsGateway: IDinInOptionsGateway
) {

    suspend fun enableTable(tableId: Int, checkId: Long) =
        dinInOptionsGateway.enableTable(tableId, checkId)

    suspend fun disableTable(tableId: Int, checkId: Long) =
        dinInOptionsGateway.disableTable(tableId, checkId)

    suspend fun splitCheck(tableId: Int, checkId: Long) =
        dinInOptionsGateway.splitCheck(tableId, checkId)

    suspend fun unSplitCheck(tableId: Int, checkId: Long) =
        dinInOptionsGateway.unSplitCheck(tableId, checkId)

    suspend fun unCombineCheck(tableId: Int, checkId: Long) =
        dinInOptionsGateway.unCombineCheck(tableId, checkId)

    suspend fun void(tableId: Int, checkId: Long) =
        dinInOptionsGateway.void(tableId, checkId)

    suspend fun splitAndPay(tableId: Int, checkId: Long) =
        dinInOptionsGateway.splitAndPay(tableId, checkId)

    suspend fun shareItem(tableId: Int, checkId: Long) =
        dinInOptionsGateway.shareItem(tableId, checkId)

    suspend fun moveItemToNewCheck(tableId: Int, checkId: Long) =
        dinInOptionsGateway.moveItemToNewCheck(tableId, checkId)

    suspend fun moveTableChecks(selectedTablesIds: List<Int>, checkId: Long) =
        dinInOptionsGateway.moveTableChecks(selectedTablesIds, checkId)

    suspend fun combineCheck(selectedTablesIds: List<Int>, checkId: Long) =
        dinInOptionsGateway.combineCheck(selectedTablesIds, checkId)

    suspend fun moveItem(selectedTablesIds: List<Int>, checkId: Long) =
        dinInOptionsGateway.moveItem(selectedTablesIds, checkId)


}