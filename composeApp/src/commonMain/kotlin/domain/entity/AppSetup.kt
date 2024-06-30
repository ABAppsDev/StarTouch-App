package domain.entity

import data.remote.model.AdjustmentSetup
import data.remote.model.TaxSetup
import kotlinx.datetime.LocalDateTime

data class AppSetup(
    val systemDate: LocalDateTime,
    val outletName: String,
    val defaultLanguage: String,
    val taxSetup: List<TaxSetup>,
    val adjustmentSetup: List<AdjustmentSetup>,
)