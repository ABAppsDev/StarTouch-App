package domain.gateway

import domain.entity.UserApp

interface IPermissionGateway {
    suspend fun checkDinInPermission(outletID: Int, restID: Int, passcode: String): UserApp
    suspend fun checkTakeAwayPermission(outletID: Int, restID: Int, passcode: String): UserApp
    suspend fun checkExitPermission(outletID: Int, restID: Int, passcode: String): Boolean
}