package domain.gateway

import domain.entity.AppSetup
import domain.entity.OutletSetup
import domain.entity.RestaurantSetup
import domain.entity.RoomSetup

interface ISetupGateway {
    suspend fun getAppSetup(outletID: Int, restID: Int): AppSetup
    suspend fun getAllRestaurants(): List<RestaurantSetup>
    suspend fun getAllOutletsByRestId(restID: Int): List<OutletSetup>
    suspend fun getAllRoomsByOutletAndRestId(outletID: Int, restID: Int): List<RoomSetup>
}