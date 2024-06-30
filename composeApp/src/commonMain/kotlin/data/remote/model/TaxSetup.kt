package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TaxSetup(
    val id: Int = 0,
    val isTakeAway: Boolean = false,
    val isDinIn: Boolean = false,
)