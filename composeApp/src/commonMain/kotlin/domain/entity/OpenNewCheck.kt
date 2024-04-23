package domain.entity

import kotlinx.datetime.LocalDateTime

data class OpenNewCheck(
    val tableId: Int,
    val tableName: String,
    val outletId: Int,
    val restId: Int,
    val workStationId: Int,
    val covers: Int,
    val dateTime: LocalDateTime,
    val userId: Int,
    val serverId: Int,
    val checkId: Long? = null,
)