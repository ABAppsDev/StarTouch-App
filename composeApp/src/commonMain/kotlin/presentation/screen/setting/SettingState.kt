package presentation.screen.setting

import androidx.compose.runtime.Immutable
import domain.entity.OutletSetup
import domain.entity.RestaurantSetup
import domain.entity.RoomSetup
import presentation.base.ErrorState
import presentation.screen.composable.DropDownState

@Immutable
data class SettingState(
    val errorMessage: String = "",
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val outlets: List<SetupItemState> = emptyList(),
    val selectedOutlet: SetupItemState = SetupItemState(),
    val selectedRestaurant: SetupItemState = SetupItemState(),
    val selectedMainRoom: SetupItemState = SetupItemState(),
    val restaurants: List<SetupItemState> = emptyList(),
    val rooms: List<SetupItemState> = emptyList(),
    val isQuickSaleLoopBack: Boolean = false,
    val apiUrl: String = "",
    val workStationId: String = "0",
    val isCallCenter: Boolean = false,
)

@Immutable
data class SetupState(
    val setupItems: List<SetupItemState> = emptyList()
)

@Immutable
data class SetupItemState(
    val id: Int = 0,
    val name: String = ""
)

fun RestaurantSetup.toSetupItemState(): SetupItemState = SetupItemState(
    id, name
)

fun OutletSetup.toSetupItemState(): SetupItemState = SetupItemState(
    id, name
)

fun RoomSetup.toSetupItemState(): SetupItemState = SetupItemState(
    id, name
)

fun SetupItemState.toDropDownState(): DropDownState = DropDownState(
    id, name
)