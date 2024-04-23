package domain.entity

data class TableData(
    val id: Int,
    val name: String,
    val coversCapacity: Int,
    val covers: Int,
    val countChecks: Int,
    val checkId: Long? = null,
    val checksAmount: Double,
    val openIn: String? = null,
    val printed: Boolean,
)