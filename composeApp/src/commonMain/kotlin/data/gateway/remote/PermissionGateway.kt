package data.gateway.remote

import data.remote.mapper.toDto
import data.remote.model.ServerResponse
import data.remote.model.UserAppDto
import domain.entity.UserApp
import domain.gateway.IPermissionGateway
import domain.util.PermissionDeniedException
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post

class PermissionGateway(client: HttpClient) : BaseGateway(client), IPermissionGateway {
    override suspend fun checkDinInPermission(
        outletID: Int,
        restID: Int,
        passcode: String
    ): UserApp {
        return tryToExecute<ServerResponse<UserAppDto>> {
            post("dinin") {
                parameter("passcode", passcode)
                parameter("restID", restID)
                parameter("outletID", outletID)
            }
        }.data?.toDto() ?: throw PermissionDeniedException("Logon error")
    }

    override suspend fun checkExitPermission(
        outletID: Int,
        restID: Int,
        passcode: String
    ): Boolean {
        return tryToExecute<ServerResponse<Boolean>> {
            post("exit") {
                parameter("passcode", passcode)
                parameter("outletID", outletID)
                parameter("restID", restID)
            }
        }.data ?: false
    }
}