package data.util

import kotlinx.coroutines.flow.MutableStateFlow
import util.LanguageCode
import util.getDateNow

object AppLanguage {
    val code: MutableStateFlow<String> = MutableStateFlow(LanguageCode.EN.value)
}

object StarTouchSetup {
    var OUTLET_ID = 0
    var MAIN_ROOM_ID = 1
    var REST_ID = 0
    var USER_ID = 0
    var SERVER_ID = 0
    var TOKEN = ""
    var WORK_STATION_ID = 0
    var USER_LANGUAGE = LanguageCode.EN.value
    var DEFAULT_LANGUAGE = LanguageCode.EN.value
    var SYSTEM_DATE = getDateNow()
    var ORDER_NUMBER = 1
}