package data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceResponseDto(
    val name: String? = null,
    val code: Int? = null,
    val myLanguage: String? = null,
    @SerialName("tital")
    val title: String? = null,
)