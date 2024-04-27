package data.gateway.remote

import data.remote.mapper.toEntity
import data.remote.model.AssignCheckDto
import data.remote.model.ServerResponse
import data.remote.model.TableDataDto
import data.util.StarTouchSetup
import domain.entity.AssignCheck
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
                parameter("ws", StarTouchSetup.WORK_STATION_ID)
                parameter("userId", StarTouchSetup.USER_ID)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Tables not found")
    }

    override suspend fun getAllTablesGuest(outletID: Int, restID: Int): List<TableData> {
        return tryToExecute<ServerResponse<List<TableDataDto>>> {
            get("/dinin/tables-guest") {
                parameter("outletID", outletID)
                parameter("restID", restID)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Tables not found")
    }

    override suspend fun getAllOnlineUsers(
        outletId: Int,
        restId: Int,
        userID: Int
    ): List<AssignCheck> {
        return tryToExecute<ServerResponse<List<AssignCheckDto>>> {
            get("/check/assign") {
                parameter("outletID", outletId)
                parameter("restID", restId)
                parameter("userID", userID)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Users not found")
    }

    override suspend fun getTablesDataByRoomId(
        outletID: Int,
        restID: Int,
        roomID: Int
    ): List<TableData> {
        return tryToExecute<ServerResponse<List<TableDataDto>>> {
            get("/dinin/room/$roomID/tables") {
                parameter("outletID", outletID)
                parameter("restID", restID)
                parameter("ws", StarTouchSetup.WORK_STATION_ID)
                parameter("userId", StarTouchSetup.USER_ID)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Tables not found")
    }
}