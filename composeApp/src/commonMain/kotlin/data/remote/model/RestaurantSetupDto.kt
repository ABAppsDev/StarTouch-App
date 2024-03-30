package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantSetupDto(
    val id: Int? = null,
    val code: Int? = null,
    val name: String? = null,
)