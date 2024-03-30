package data.remote.mapper

import data.remote.model.AssignDrawerDto
import domain.entity.AssignDrawer


fun AssignDrawerDto.toEntity(): AssignDrawer = AssignDrawer(
    id = id ?: 0,
    name = name ?: ""
)