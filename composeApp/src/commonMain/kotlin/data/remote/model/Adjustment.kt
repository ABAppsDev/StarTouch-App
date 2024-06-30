package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Adjustment(
    val id: Int,
    val type: String,
    val value: Float,
    val minPerson: Int,
    val forEachPerson: Boolean,
    val isDefault: Boolean,
    val minCharge: Boolean,
    val openAmount: Boolean,
    val taxable: Boolean,
)