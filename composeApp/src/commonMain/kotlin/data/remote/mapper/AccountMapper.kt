package data.remote.mapper

import data.remote.model.AttendanceRequestDto
import domain.entity.Account

fun Account.toAttendanceRequestDto(): AttendanceRequestDto = AttendanceRequestDto(
    userName = username,
    password = password,
    restID = restID,
    outletID = outletID
)