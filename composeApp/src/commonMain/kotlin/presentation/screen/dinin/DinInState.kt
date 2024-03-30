package presentation.screen.dinin

import androidx.compose.runtime.Immutable
import domain.entity.AssignDrawer
import domain.entity.TableData
import presentation.base.ErrorState

@Immutable
data class DinInState(
    val errorMessage: String = "",
    val errorDinInState: ErrorState? = null,
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val tableId: Int = 0,
    val tableName: Int = 0,
    val dinInDialogueState: DinInDialogueState = DinInDialogueState(),
    val tablesDetails: List<TableDetailsState> = emptyList(),
    val errorDialogueIsVisible: Boolean = false,
    val warningDialogueIsVisible: Boolean = false,
    val isRefreshing: Boolean = false,
    val showErrorScreen: Boolean = false,
    val checkId: Long = 0,
)

@Immutable
data class TableDetailsState(
    val tableNumber: Int = 0,
    val tableId: Int = 0,
    val totalOrdersPrice: Double = 0.0,
    val openCheckDate: String? = null,
    val checkId: Long? = null,
    val coversCount: Int = 0,
    val covers: Int = 0,
    val checksCount: Int = 0,
)

@Immutable
data class DinInDialogueState(
    val isLoading: Boolean = false,
    val isLoadingButton: Boolean = false,
    val isVisible: Boolean = false,
    val coversCount: String = "",
    val serverId: Int = 0,
    val assignDrawers: List<AssignDrawerState> = emptyList(),
    val checks: List<AssignDrawerState> = emptyList(),
    val isSuccess: Boolean = false,
)

@Immutable
data class AssignDrawerState(
    val id: Long = 0,
    val name: String = "",
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

fun AssignDrawer.toAssignDrawerState(): AssignDrawerState = AssignDrawerState(
    id.toLong(), name
)