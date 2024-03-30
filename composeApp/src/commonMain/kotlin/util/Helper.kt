package util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getDateNow() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun getLanguageCodeByName(name: String): LanguageCode {
    return if (name == Constants.ARABIC) LanguageCode.AR else LanguageCode.EN
}