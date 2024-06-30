package data.remote.model

import data.util.AdjustmentSetup
import data.util.TaxSetup
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AppSetupDto(
    val systemDate: LocalDateTime? = null,
    val outletName: String? = null,
    val defaultLanguage: String? = null,
    val taxSetup: List<TaxSetup>,
    val adjustmentSetup: List<AdjustmentSetup>,
)