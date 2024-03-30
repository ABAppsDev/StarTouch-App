package data.gateway.remote

import data.remote.mapper.toDto
import data.remote.mapper.toEntity
import data.remote.model.OpenCheckDto
import data.remote.model.OpenNewCheckDto
import data.remote.model.ServerResponse
import domain.entity.FireItems
import domain.entity.OpenCheck
import domain.entity.OpenNewCheck
import domain.gateway.IChecksGateway
import domain.util.NotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.util.InternalAPI
import kotlinx.serialization.json.Json

class ChecksGateway(client: HttpClient) : BaseGateway(client), IChecksGateway {
    @OptIn(InternalAPI::class)
    override suspend fun openNewCheck(openNewCheck: OpenNewCheck): OpenCheck {
        return tryToExecute<ServerResponse<OpenCheckDto>> {
            post("/takeaway/new-check") {
                val openNewCheckDto = openNewCheck.toDto()
                body = Json.encodeToString(OpenNewCheckDto.serializer(), openNewCheckDto)
            }
        }.data?.toEntity() ?: throw NotFoundException("Check not found")
    }

    override suspend fun fireItems(
        checkID: Long,
        serverId: Int,
        userID: String, items: List<FireItems>
    ): Boolean {
        return tryToExecute<ServerResponse<Boolean>> {
            post("/fire") {
                setBody(items.map { it.toDto() })
                parameter("checkID", checkID)
                parameter("userID", serverId)
                parameter("serverID", userID)
            }
        }.data ?: throw Exception("")
    }
}