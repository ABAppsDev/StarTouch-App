package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AssignCheckDto(
    val id: Int? = null,
    val name: String? = null,
)
