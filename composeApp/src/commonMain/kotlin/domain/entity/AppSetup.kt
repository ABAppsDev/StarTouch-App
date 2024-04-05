package domain.entity

import kotlinx.datetime.LocalDateTime

data class AppSetup(
    val systemDate: LocalDateTime,
    val outletName: String,
    val defaultLanguage: String,
)