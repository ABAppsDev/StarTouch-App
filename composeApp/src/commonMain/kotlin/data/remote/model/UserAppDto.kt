package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UserAppDto(
    val id: Int? = null,
    val language: String? = null,
    val token: String? = null,
)
