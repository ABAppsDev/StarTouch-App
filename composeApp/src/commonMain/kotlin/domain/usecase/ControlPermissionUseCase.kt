package domain.usecase

import domain.entity.UserApp
import domain.gateway.IPermissionGateway

class ControlPermissionUseCase(
    private val permissionGateway: IPermissionGateway,
) {
    suspend fun checkDinInPermission(outletID: Int, restID: Int, passcode: String): UserApp =
        permissionGateway.checkDinInPermission(outletID, restID, passcode)

    suspend fun checkTakeAwayPermission(outletID: Int, restID: Int, passcode: String): UserApp =
        permissionGateway.checkTakeAwayPermission(outletID, restID, passcode)

    suspend fun checkExitAppPermission(outletID: Int, restID: Int, passcode: String): Boolean =
        permissionGateway.checkExitPermission(outletID, restID, passcode)
}