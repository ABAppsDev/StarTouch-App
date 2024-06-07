package data.gateway.remote

import data.remote.mapper.toEntity
import data.remote.model.AppSetupDto
import data.remote.model.OutletSetupDto
import data.remote.model.RestaurantSetupDto
import data.remote.model.RoomSetupDto
import data.remote.model.ServerResponse
import data.util.StarTouchSetup
import domain.entity.AppSetup
import domain.entity.OutletSetup
import domain.entity.RestaurantSetup
import domain.entity.RoomSetup
import domain.gateway.ISetupGateway
import domain.util.NotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SetupGateway(client: HttpClient) : BaseGateway(client), ISetupGateway {
    override suspend fun getAppSetup(outletID: Int, restID: Int): AppSetup {
        return tryToExecute<ServerResponse<AppSetupDto>> {
            get("setup") {
                parameter("outletID", outletID)
                parameter("restID", restID)
                parameter("ws", StarTouchSetup.WORK_STATION_ID)
            }
        }.data?.toEntity() ?: throw NotFoundException("Setup not found")
    }

    override suspend fun getAllRestaurants(): List<RestaurantSetup> {
        return tryToExecute<ServerResponse<List<RestaurantSetupDto>>> {
            get("setup/restaurants")
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Restaurants not found")
    }

    override suspend fun getAllOutletsByRestId(restID: Int): List<OutletSetup> {
        return tryToExecute<ServerResponse<List<OutletSetupDto>>> {
            get("setup/outlets") {
                parameter("restID", restID)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Outlets not found")
    }

    override suspend fun getAllRoomsByOutletAndRestId(outletID: Int, restID: Int): List<RoomSetup> {
        return tryToExecute<ServerResponse<List<RoomSetupDto>>> {
            get("setup/rooms") {
                parameter("outletID", outletID)
                parameter("restID", restID)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("DinIn rooms not found")
    }
}