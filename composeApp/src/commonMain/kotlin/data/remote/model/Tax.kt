package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Tax(
    val id: Int,
    val type: String,
    val value: Float,
    val minPerson: Int,
    val forEachPerson: Boolean,
    val isDefault: Boolean,
)