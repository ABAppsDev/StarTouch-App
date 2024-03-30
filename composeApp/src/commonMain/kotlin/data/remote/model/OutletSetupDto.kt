package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class OutletSetupDto(
    val id: Int? = null,
    val code: Int? = null,
    val name: String? = null,
)
