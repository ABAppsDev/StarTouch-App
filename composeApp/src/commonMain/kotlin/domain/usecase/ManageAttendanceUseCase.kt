package domain.usecase

import domain.entity.Account
import domain.entity.Attendance
import domain.gateway.IAttendanceGateway

class ManageAttendanceUseCase(
    private val attendanceGateway: IAttendanceGateway,
) {
    suspend fun login(account: Account): Attendance = attendanceGateway.login(account)

    suspend fun logout(account: Account): Attendance = attendanceGateway.logout(account)
}