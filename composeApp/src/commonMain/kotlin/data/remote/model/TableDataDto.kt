package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TableDataDto(
    val id: Int? = null,
    val name: String? = null,
    val coversCapacity: Int? = null,
    val covers: Int? = null,
    val countChecks: Int? = null,
    val checkId: Long? = null,
    val checksAmount: Double? = null,
    val openIn: String? = null,
    val printed: Boolean? = null,
)