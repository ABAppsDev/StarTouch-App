package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomSetupDto(
    val id: Int? = null,
    val code: Int? = null,
    val name: String? = null,
    val name2: String? = null,
)
