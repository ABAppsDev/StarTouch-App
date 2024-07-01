package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Tax(
    val id: Int = 0,
    val name: String = "",
    val name2: String = "",
    val type: String = "",
    val value: Float = 0.0f,
    val minPerson: Int = 0,
    val forEachPerson: Boolean = false,
    val isDinIn: Boolean = false,
    val isTakeAway: Boolean = false,
)