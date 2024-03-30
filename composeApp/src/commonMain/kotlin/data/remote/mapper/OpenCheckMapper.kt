package data.remote.mapper

import data.remote.model.OpenCheckDto
import domain.entity.OpenCheck

fun OpenCheckDto.toEntity() = OpenCheck(
    serial = serial ?: 0, id = id ?: 0L
)


fun OpenCheck.toDto() = OpenCheckDto(serial, id)