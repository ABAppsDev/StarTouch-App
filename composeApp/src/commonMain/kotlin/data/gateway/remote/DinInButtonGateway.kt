package data.gateway.remote

import data.remote.mapper.toDto
import data.remote.mapper.toEntity
import data.remote.model.FireItemsDto
import data.remote.model.ServerResponse
import data.util.StarTouchSetup
import domain.entity.FireItems
import domain.gateway.IDinInButtonGateway
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class DinInButtonGateway(client: HttpClient) : BaseGateway(client), IDinInButtonGateway {
    override suspend fun getTableItems(
        checkId: Long,
        tableId: Long,
    ): List<FireItems> {
        return tryToExecute<ServerResponse<List<FireItemsDto>>> {
            get("/dinin-buttons/get-table-items") {
                parameter("outletID", StarTouchSetup.OUTLET_ID)
                parameter("restID", StarTouchSetup.REST_ID)
                parameter("userId", StarTouchSetup.USER_ID)
                parameter("checkId", checkId)
                parameter("tableId", tableId)
            }
        }.data?.map { it.toEntity() } ?: throw Exception("GEGE")
    }

    override suspend fun moveItems(fromCheckId: Long, toCheckId: Long, itemSerials: List<Int>) {
        tryToExecute<ServerResponse<Boolean>> {
            post("/dinin-buttons/move-items") {
                parameter("outletID", StarTouchSetup.OUTLET_ID)
                parameter("restID", StarTouchSetup.REST_ID)
                parameter("userId", StarTouchSetup.USER_ID)
                parameter("ws", StarTouchSetup.WORK_STATION_ID)
                parameter("fromCheckId", fromCheckId)
                parameter("toCheckId", toCheckId)
                setBody(itemSerials)
            }
        }.data ?: throw Exception("Exception on Move Items")
    }
}