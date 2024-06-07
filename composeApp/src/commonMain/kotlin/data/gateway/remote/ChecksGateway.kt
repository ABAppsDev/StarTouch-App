package data.gateway.remote

import data.remote.mapper.toDto
import data.remote.mapper.toEntity
import data.remote.model.FireItemsDto
import data.remote.model.OpenCheckDto
import data.remote.model.OpenNewCheckDto
import data.remote.model.ReOpenCheck
import data.remote.model.ServerResponse
import data.util.StarTouchSetup
import domain.entity.FireItems
import domain.entity.OpenCheck
import domain.entity.OpenNewCheck
import domain.gateway.IChecksGateway
import domain.gateway.local.ILocalConfigurationGateway
import domain.util.NotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.util.InternalAPI
import kotlinx.serialization.json.Json

class ChecksGateway(
    client: HttpClient,
    private val localConfigurationGateway: ILocalConfigurationGateway,
) : BaseGateway(client), IChecksGateway {
    @OptIn(InternalAPI::class)
    override suspend fun openNewCheck(openNewCheck: OpenNewCheck): OpenCheck {
        return tryToExecute<ServerResponse<OpenCheckDto>> {
            post("/check/new") {
                val openNewCheckDto = openNewCheck.toDto()
                body = Json.encodeToString(OpenNewCheckDto.serializer(), openNewCheckDto)
            }
        }.data?.toEntity() ?: throw NotFoundException("Check not found")
    }

    @OptIn(InternalAPI::class)
    override suspend fun openNewCheckTableGuest(openNewCheck: OpenNewCheck): OpenCheck {
        return tryToExecute<ServerResponse<OpenCheckDto>> {
            post("/check/new/table-guest") {
                val openNewCheckDto = openNewCheck.toDto()
                body = Json.encodeToString(OpenNewCheckDto.serializer(), openNewCheckDto)
            }
        }.data?.toEntity() ?: throw NotFoundException("Check not found")
    }

    @OptIn(InternalAPI::class)
    override suspend fun openNewCheckWithChecksOpen(openNewCheck: OpenNewCheck): OpenCheck {
        return tryToExecute<ServerResponse<OpenCheckDto>> {
            post("/check/new/validation") {
                val openNewCheckDto = openNewCheck.toDto()
                body = Json.encodeToString(OpenNewCheckDto.serializer(), openNewCheckDto)
            }
        }.data?.toEntity() ?: throw NotFoundException("Check not found")
    }

    override suspend fun getAllChecksByTableId(
        tableId: Int,
        checkId: Long,
        outletID: Int,
        restID: Int,
        serverId: Int,
        userId: Int
    ): List<ReOpenCheck> {
        return tryToExecute<ServerResponse<List<ReOpenCheck>>> {
            get("/check/opened-checks") {
                parameter("tableId", tableId)
                parameter("checkId", checkId)
                parameter("outletID", outletID)
                parameter("restID", restID)
                parameter("userId", userId)
                parameter("serverId", serverId)
            }
        }.data ?: throw NotFoundException("Check not found")
    }

    override suspend fun makeCheckAborted(checkId: Long, outletID: Int, restID: Int) {
        tryToExecute<Unit> {
            post("/check/abort") {
                parameter("checkId", checkId)
                parameter("outletID", outletID)
                parameter("restID", restID)
                parameter("ws", StarTouchSetup.WORK_STATION_ID)
            }
        }
    }

    override suspend fun deleteTable(checkId: Long) {
        tryToExecute<Unit> {
            post("/check/delete-table") {
                parameter("checkId", checkId)
                parameter("ws", StarTouchSetup.WORK_STATION_ID)
            }
        }
    }

    override suspend fun reOpenCheck(checkId: Long, tableId: Int?): List<FireItems> {
        return tryToExecute<ServerResponse<List<FireItemsDto>>> {
            get("/check/reopen") {
                parameter("checkId", checkId)
                parameter("userId", StarTouchSetup.USER_ID)
                parameter("ws", StarTouchSetup.WORK_STATION_ID)
                parameter("outletID", StarTouchSetup.OUTLET_ID)
                parameter("restID", StarTouchSetup.REST_ID)
                parameter("tableId", tableId)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Check not found")
    }

    override suspend fun addItemsToExistCheck(
        checkID: Long,
        serverId: Int,
        userID: String,
        items: List<FireItems>
    ): Boolean {
        return tryToExecute<ServerResponse<Boolean>> {
            val fastLoop = localConfigurationGateway.getIsBackToHome()
            post("/check/fire/reopen") {
                setBody(items.map { it.toDto() })
                parameter("checkID", checkID)
                parameter("userID", serverId)
                parameter("serverID", userID)
                parameter("outletID", StarTouchSetup.OUTLET_ID)
                parameter("restID", StarTouchSetup.REST_ID)
                parameter("fastBack", !fastLoop)
            }
        }.data ?: throw Exception("")
    }

    override suspend fun fireItems(
        checkID: Long,
        serverId: Int,
        userID: String, items: List<FireItems>
    ): Boolean {
        return tryToExecute<ServerResponse<Boolean>> {
            val fastLoop = localConfigurationGateway.getIsBackToHome()
            post("/check/fire") {
                setBody(items.map { it.toDto() })
                parameter("checkID", checkID)
                parameter("userID", serverId)
                parameter("serverID", userID)
                parameter("outletID", StarTouchSetup.OUTLET_ID)
                parameter("restID", StarTouchSetup.REST_ID)
                parameter("fastBack", !fastLoop)
            }
        }.data ?: throw Exception("")
    }
}