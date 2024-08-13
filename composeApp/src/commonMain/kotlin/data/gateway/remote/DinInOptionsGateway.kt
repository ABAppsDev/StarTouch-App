package data.gateway.remote

import domain.gateway.IDinInOptionsGateway
import io.ktor.client.HttpClient

class DinInOptionsGateway(client: HttpClient) : BaseGateway(client), IDinInOptionsGateway {
    override suspend fun enableTable(tableId: Int, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun disableTable(tableId: Int, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun splitCheck(tableId: Int, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun unSplitCheck(tableId: Int, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun unCombineCheck(tableId: Int, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun void(tableId: Int, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun splitAndPay(tableId: Int, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun shareItem(tableId: Int, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun moveItemToNewCheck(tableId: Int, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun moveTableChecks(selectedTablesIds: List<Int>, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun combineCheck(selectedTablesIds: List<Int>, checkId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun moveItem(selectedTablesIds: List<Int>, checkId: Long) {
        TODO("Not yet implemented")
    }

}