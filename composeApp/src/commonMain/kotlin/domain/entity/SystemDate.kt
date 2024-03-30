package domain.entity

import kotlinx.datetime.LocalDateTime

data class SystemDate(
    val id: Int,
    val sysDate: LocalDateTime?,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val status: String?,
    val userId: Int?,
    val restIdActive: Int?,
    val outLetIdActive: Int?
)