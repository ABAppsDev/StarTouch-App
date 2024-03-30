package data.remote.mapper

import data.remote.model.AttendanceResponseDto
import domain.entity.Attendance

fun AttendanceResponseDto.toEntity(): Attendance = Attendance(
    name = name ?: "",
    code = code ?: 0,
    title = title ?: "",
    language = myLanguage ?: "",
)