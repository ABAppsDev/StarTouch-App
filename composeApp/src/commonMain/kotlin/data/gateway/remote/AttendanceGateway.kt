package data.gateway.remote

import data.remote.mapper.toAttendanceRequestDto
import data.remote.mapper.toEntity
import data.remote.model.AttendanceRequestDto
import data.remote.model.AttendanceResponseDto
import data.remote.model.ServerResponse
import domain.entity.Account
import domain.entity.Attendance
import domain.gateway.IAttendanceGateway
import domain.util.NotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.util.InternalAPI
import kotlinx.serialization.json.Json

class AttendanceGateway(client: HttpClient) : BaseGateway(client), IAttendanceGateway {
    @OptIn(InternalAPI::class)
    override suspend fun login(account: Account): Attendance {
        return tryToExecute<ServerResponse<AttendanceResponseDto>> {
            post("attendance/login") {
                val attendanceRequestDto = account.toAttendanceRequestDto()
                body = Json.encodeToString(AttendanceRequestDto.serializer(), attendanceRequestDto)
            }
        }.data?.toEntity() ?: throw NotFoundException("User not found")
    }

    @OptIn(InternalAPI::class)
    override suspend fun logout(account: Account): Attendance {
        return tryToExecute<ServerResponse<AttendanceResponseDto>> {
            post("attendance/logout") {
                val attendanceRequestDto = account.toAttendanceRequestDto()
                body = Json.encodeToString(AttendanceRequestDto.serializer(), attendanceRequestDto)
            }
        }.data?.toEntity() ?: throw NotFoundException("User not found")
    }
}