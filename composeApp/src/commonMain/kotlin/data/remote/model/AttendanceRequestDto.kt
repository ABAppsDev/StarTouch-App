package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRequestDto(
    val userName: String? = null,
    val password: String? = null,
    val restID: Int? = null,
    val outletID: Int? = null,
)