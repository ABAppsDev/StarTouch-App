package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class FireItemsDto(
    val totalPrice: Float? = null,
    val noServiceCharge: Boolean? = null,
    val isModifier: Boolean? = null,
    val taxable: Boolean? = null,
    val pickFollowItemQty: Boolean? = null,
    val modifierGroupID: Int? = null,
    val prePaidCard: Boolean? = null,
    val ws: Int? = null,
    val id: Int? = null,
    val price: Float? = null,
    val qty: Int? = null,
)