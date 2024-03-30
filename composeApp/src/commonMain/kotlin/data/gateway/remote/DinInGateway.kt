package data.gateway.remote

import data.remote.mapper.toEntity
import data.remote.model.AssignDrawerDto
import data.remote.model.ServerResponse
import data.remote.model.TableDataDto
import domain.entity.AssignDrawer
import domain.entity.TableData
import domain.gateway.IDinInGateway
import domain.util.NotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DinInGateway(client: HttpClient) : BaseGateway(client), IDinInGateway {
    override suspend fun getTableData(outletID: Int, restID: Int): List<TableData> {
        return tryToExecute<ServerResponse<List<TableDataDto>>> {
            get("/dinin/tables") {
                parameter("outletID", outletID)
                parameter("restID", restID)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Tables not found")
    }

    override suspend fun getAllOnlineUsers(outletId: Int, restId: Int): List<AssignDrawer> {
        return tryToExecute<ServerResponse<List<AssignDrawerDto>>> {
            get("/dinin/assign-drawer") {
                parameter("outletID", outletId)
                parameter("restID", restId)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Users not found")
    }
}