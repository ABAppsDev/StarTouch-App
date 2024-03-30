package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AssignDrawerDto(
    val id: Int? = null,
    val name: String? = null,
)
