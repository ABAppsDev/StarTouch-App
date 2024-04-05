package data.remote.mapper

import data.remote.model.AssignCheckDto
import domain.entity.AssignCheck


fun AssignCheckDto.toEntity(): AssignCheck = AssignCheck(
    id = id ?: 0,
    name = name ?: ""
)