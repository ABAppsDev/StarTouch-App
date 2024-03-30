package data.local.model

import io.realm.kotlin.types.RealmObject

class AppConfigurationCollection : RealmObject {
    var id: Int = 0
    var apiUrl: String = ""
    var outletId: Int = 0
    var restId: Int = 0
    var workStationId: Int = 0
    var isCallCenter: Boolean = false
    var isReturnedHomeAfterFinishOrder: Boolean = true
    var hasMoreButtonClick: Boolean = false
    var dinInMainRoomId: Int = 1
}