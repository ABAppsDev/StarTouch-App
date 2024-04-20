package presentation.screen.dinin

import androidx.compose.runtime.Immutable
import data.util.AppLanguage
import data.util.StarTouchSetup
import domain.entity.AssignCheck
import domain.entity.RoomSetup
import domain.entity.TableData
import presentation.base.ErrorState
import util.LanguageCode

@Immutable
data class DinInState(
    val errorMessage: String = "",
    val errorDinInState: ErrorState? = null,
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val tableId: Int = 0,
    val tableName: String = "0",
    val dinInDialogueState: DinInDialogueState = DinInDialogueState(),
    val tablesDetails: List<TableDetailsState> = emptyList(),
    val errorDialogueIsVisible: Boolean = false,
    val warningDialogueIsVisible: Boolean = false,
    val isRefreshing: Boolean = false,
    val showErrorScreen: Boolean = false,
    val checkId: Long = 0,
    val rooms: List<RoomDetailsState> = emptyList(),
    val roomId: Int = StarTouchSetup.MAIN_ROOM_ID,
)

@Immutable
data class TableDetailsState(
    val tableNumber: String = "0",
    val tableId: Int = 0,
    val totalOrdersPrice: Double = 0.0,
    val openCheckDate: String? = null,
    val checkId: Long? = null,
    val coversCount: Int = 0,
    val covers: Int = 0,
    val checksCount: Int = 0,
)

@Immutable
data class RoomDetailsState(
    val id: Int = 0,
    val name: String = "",
    val code: Int = 0,
)

@Immutable
data class DinInDialogueState(
    val isLoading: Boolean = false,
    val isLoadingButton: Boolean = false,
    val isVisible: Boolean = false,
    val coversCount: String = "",
    val tableName: String = "",
    val serverId: Int = 0,
    val assignDrawers: List<AssignCheckState> = emptyList(),
    val checks: List<AssignCheckState> = emptyList(),
    val isSuccess: Boolean = false,
    val isNamedTable: Boolean = false,
)

@Immutable
data class AssignCheckState(
    val id: Long = 0,
    val name: String = "",
    val tableName:String = "",
    val status:String = ""
)


fun TableData.toTableDetailsState(): TableDetailsState = TableDetailsState(
    tableNumber = this.name,
    tableId = this.id,
    totalOrdersPrice = this.checksAmount,
    coversCount = this.coversCapacity,
    covers = this.covers,
    checkId = this.checkId,
    openCheckDate = this.openIn ?: "",
    checksCount = this.countChecks
)

fun AssignCheck.toAssignDrawerState(): AssignCheckState = AssignCheckState(
    id.toLong(), name
)

fun RoomSetup.toState(): RoomDetailsState = RoomDetailsState(
    id, if (AppLanguage.code.value == LanguageCode.EN.value) name else name2, code,
)