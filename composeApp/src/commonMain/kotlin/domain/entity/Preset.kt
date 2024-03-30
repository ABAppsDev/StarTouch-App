package domain.entity

import kotlinx.datetime.LocalDateTime

data class Preset(
    val id: Int,
    val code: String,
    val name: String,
    val name2: String,
    val description: String,
    val imagePreset: ByteArray?,
    val active: Boolean,
    val createDate: LocalDateTime,
    val modifiedDate: LocalDateTime,
    val userID: Int,
    val restIDActive: Int,
    val outLetIDActive: Int,
    val byTime: Boolean?,
    val startTime: Int?,
    val endTime: Int?
)