package data.remote.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AppSetupDto(
    val systemDate: LocalDateTime? = null,
    val outletName: String? = null,
    val defaultLanguage: String? = null,
    val taxes: List<Tax>,
    val adjustments: List<Adjustment>,
)