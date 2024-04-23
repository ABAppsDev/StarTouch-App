package data.remote.mapper

import data.remote.model.OpenNewCheckDto
import data.util.StarTouchSetup
import domain.entity.OpenNewCheck

fun OpenNewCheckDto.toEntity(): OpenNewCheck = OpenNewCheck(
    tableId = tableId ?: 0,
    tableName = tableName ?: "0",
    dateTime = dateTime ?: StarTouchSetup.SYSTEM_DATE,
    userId = userId ?: 0,
    serverId = serverId ?: 0,
    covers = covers ?: 0,
    workStationId = workStationId ?: 0,
    restId = restId ?: 0,
    outletId = outletId ?: 0,
    checkId = checkId ?: 0
)


fun OpenNewCheck.toDto(): OpenNewCheckDto = OpenNewCheckDto(
    tableId = tableId,
    tableName = tableName,
    dateTime = dateTime,
    userId = userId,
    serverId = serverId,
    covers = covers,
    workStationId = workStationId,
    restId = restId,
    outletId = outletId,
    checkId = checkId,
)