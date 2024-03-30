package data.remote.mapper

import data.remote.model.UserAppDto
import domain.entity.UserApp

fun UserAppDto.toDto(): UserApp = UserApp(
    id ?: 0,
    language ?: "English",
    token ?: "",
)