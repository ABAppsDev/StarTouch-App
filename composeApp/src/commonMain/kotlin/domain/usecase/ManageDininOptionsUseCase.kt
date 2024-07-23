package domain.usecase

import presentation.screen.dinin.TableDetailsState

class ManageDininOptionsUseCase {

    fun enableTable(tableId: Int, listOfTables: List<TableDetailsState>): List<TableDetailsState> {
        val indexOfSelectedTable = listOfTables.indexOfLast { it.tableId == tableId }
        val selectedTable = listOfTables[indexOfSelectedTable]
        val updatedList = listOfTables.toMutableList()
        updatedList[indexOfSelectedTable] = selectedTable.copy(enabled = true)
        return updatedList
    }
    fun disableTable(tableId: Int, listOfTables: List<TableDetailsState>): List<TableDetailsState> {
        val indexOfSelectedTable = listOfTables.indexOfLast { it.tableId == tableId }
        val selectedTable = listOfTables[indexOfSelectedTable]
        val updatedList = listOfTables.toMutableList()
        updatedList[indexOfSelectedTable] = selectedTable.copy(enabled = false)
        return updatedList
    }
    fun moveTableChecks(selectedTablesIds: List<Int>, listOfTables: List<TableDetailsState>): List<TableDetailsState> {
        val indexOfFirstSelectedTable = listOfTables.indexOfLast { it.tableId == selectedTablesIds.first() }
        val indexOfSecondSelectedTable = listOfTables.indexOfLast { it.tableId == selectedTablesIds.last() }
        val selectedFirstTable = listOfTables[indexOfFirstSelectedTable]
        val selectedSecondTable = listOfTables[indexOfSecondSelectedTable]
        val updatedList = listOfTables.toMutableList()
        updatedList[indexOfFirstSelectedTable] = selectedFirstTable.copy(tableNumber = selectedSecondTable.tableNumber)
        updatedList[indexOfSecondSelectedTable] = selectedSecondTable.copy(tableNumber = selectedFirstTable.tableNumber)
        return updatedList
    }

}