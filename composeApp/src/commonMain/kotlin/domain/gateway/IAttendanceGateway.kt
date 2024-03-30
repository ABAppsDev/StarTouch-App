package domain.gateway

import domain.entity.Account
import domain.entity.Attendance

interface IAttendanceGateway {
    suspend fun login(account: Account): Attendance
    suspend fun logout(account: Account): Attendance
}