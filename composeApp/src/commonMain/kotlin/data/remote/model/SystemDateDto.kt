package data.remote.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class SystemDateDto(
    val id: Int? = null,
    val sysDate: LocalDateTime? = null,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val status: String? = null,
    val userId: Int? = null,
    val restIdActive: Int? = null,
    val outLetIdActive: Int? = null,
)