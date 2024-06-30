package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Tax(
    val id: Int = 0,
    val type: String = "",
    val value: Float = 0.0f,
    val minPerson: Int = 0,
    val forEachPerson: Boolean = false,
    val isDefault: Boolean = false,
    val isDinIn: Boolean = false,
    val isTakeAway: Boolean = false,
)