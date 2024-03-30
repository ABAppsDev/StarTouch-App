package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class OpenCheckDto(
    val serial: Int? = null,
    val id: Long? = null,
)
