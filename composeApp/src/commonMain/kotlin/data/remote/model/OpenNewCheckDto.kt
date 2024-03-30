package data.remote.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class OpenNewCheckDto(
    val tableId: Int? = null,
    val tableName: String? = null,
    val outletId: Int? = null,
    val restId: Int? = null,
    val workStationId: Int? = null,
    val covers: Int? = null,
    val dateTime: LocalDateTime? = null,
    val userId: Int? = null,
    val serverId: Int? = null,
    val checkType: String? = null,
)
