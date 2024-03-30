package data.remote.mapper

import data.remote.model.PresetsDto
import domain.entity.Preset
import util.getDateNow

fun PresetsDto.toEntity(): Preset = Preset(
    id = id ?: 0,
    code = code ?: "0",
    name = name ?: "",
    name2 = name2 ?: "",
    description = description ?: "",
    imagePreset = imagePreset,
    active = active ?: false,
    createDate = createDate ?: getDateNow(),
    modifiedDate = modifiedDate ?: getDateNow(),
    userID = userID ?: 0,
    restIDActive = restIDActive ?: 0,
    outLetIDActive = outLetIDActive ?: 0,
    byTime = byTime,
    startTime = startTime,
    endTime = endTime,
)