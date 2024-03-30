package domain.usecase

import domain.entity.AppSetup
import domain.entity.OutletSetup
import domain.entity.RestaurantSetup
import domain.entity.RoomSetup
import domain.gateway.ISetupGateway
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class GetAppSetupUseCase(
    private val setupGateway: ISetupGateway
) {
    suspend operator fun invoke(outletID: Int, restID: Int): AppSetup {
        return if (outletID <= 0 || restID <= 0)
            AppSetup(
                systemDate = LocalDateTime(
                    LocalDate(1000, 1, 1),
                    LocalTime(0, 0, 0)
                ),
                outletName = "ABApps",
                defaultLanguage = "English"
            )
        else setupGateway.getAppSetup(outletID, restID)
    }

    suspend fun getAllRestaurants(): List<RestaurantSetup> = setupGateway.getAllRestaurants()

    suspend fun getAllOutletsByRestId(restID: Int): List<OutletSetup> {
        return setupGateway.getAllOutletsByRestId(restID)
    }

    suspend fun getAllRoomsByOutletAndRestId(outletID: Int, restID: Int): List<RoomSetup> {
        return setupGateway.getAllRoomsByOutletAndRestId(outletID, restID)
    }
}