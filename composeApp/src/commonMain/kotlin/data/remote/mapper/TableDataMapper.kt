package data.remote.mapper

import data.remote.model.TableDataDto
import domain.entity.TableData

fun TableDataDto.toEntity(): TableData = TableData(
    id = id ?: 0,
    name = name ?: "0",
    covers = covers ?: 0,
    coversCapacity = coversCapacity ?: 0,
    checkId = checkId,
    checksAmount = checksAmount ?: 0.0,
    countChecks = countChecks ?: 0,
    openIn = openIn ?: "",
    printed = printed ?: false,
)