package domain.entity

data class FireItems(
    val id: Int,
    val price: Float,
    val totalPrice: Float,
    val qty: Int,
    val noServiceCharge: Boolean,
    val isModifier: Boolean,
    val taxable: Boolean,
    val pickFollowItemQty: Boolean,
    val modifierGroupID: Int,
    val prePaidCard: Boolean,
    val ws: Int,
)