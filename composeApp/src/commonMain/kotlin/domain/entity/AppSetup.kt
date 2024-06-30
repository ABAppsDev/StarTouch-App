package domain.entity

import data.remote.model.Adjustment
import data.remote.model.Tax
import kotlinx.datetime.LocalDateTime

data class AppSetup(
    val systemDate: LocalDateTime,
    val outletName: String,
    val defaultLanguage: String,
    val taxes: List<Tax>,
    val adjustments: List<Adjustment>,
)