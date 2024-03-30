package domain.entity

data class TableData(
    val id: Int,
    val name: Int,
    val coversCapacity: Int,
    val covers: Int,
    val countChecks: Int,
    val checkId: Long? = null,
    val checksAmount: Double,
    val openIn: String? = null,
)