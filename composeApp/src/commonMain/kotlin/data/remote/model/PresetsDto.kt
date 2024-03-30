package data.remote.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PresetsDto(
    val id: Int? = null,
    val code: String? = null,
    val name: String? = null,
    val name2: String? = null,
    val description: String? = null,
    val imagePreset: ByteArray? = null,
    val active: Boolean? = null,
    val createDate: LocalDateTime? = null,
    val modifiedDate: LocalDateTime? = null,
    val userID: Int? = null,
    val restIDActive: Int? = null,
    val outLetIDActive: Int? = null,
    val byTime: Boolean? = null,
    val startTime: Int? = null,
    val endTime: Int? = null
)