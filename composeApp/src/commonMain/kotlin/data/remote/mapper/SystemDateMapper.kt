package data.remote.mapper

import data.remote.model.SystemDateDto
import domain.entity.SystemDate

fun SystemDate.toDTO(): SystemDateDto {
    return SystemDateDto(
        id = id,
        sysDate = sysDate,
        startTime = startTime,
        endTime = endTime,
        status = status,
        userId = userId,
        restIdActive = restIdActive,
        outLetIdActive = outLetIdActive
    )
}


fun SystemDateDto.toEntity(): SystemDate {
    return SystemDate(
        id = id ?: 0,
        sysDate = sysDate,
        startTime = startTime,
        endTime = endTime,
        status = status,
        userId = userId,
        restIdActive = restIdActive,
        outLetIdActive = outLetIdActive
    )
}