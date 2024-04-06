package data.remote.mapper

import data.remote.model.AppSetupDto
import data.remote.model.OutletSetupDto
import data.remote.model.RestaurantSetupDto
import data.remote.model.RoomSetupDto
import domain.entity.AppSetup
import domain.entity.OutletSetup
import domain.entity.RestaurantSetup
import domain.entity.RoomSetup
import util.getDateNow

fun AppSetupDto.toEntity(): AppSetup = AppSetup(
    outletName = outletName ?: "",
    systemDate = systemDate ?: getDateNow(),
    defaultLanguage = defaultLanguage ?: "English",

)

fun RestaurantSetupDto.toEntity(): RestaurantSetup = RestaurantSetup(
    id ?: 0, code ?: 0, name ?: ""
)

fun OutletSetupDto.toEntity(): OutletSetup = OutletSetup(
    id ?: 0, code ?: 0, name ?: ""
)

fun RoomSetupDto.toEntity(): RoomSetup = RoomSetup(
    id ?: 0, code ?: 0, name ?: "",name2?:""
)