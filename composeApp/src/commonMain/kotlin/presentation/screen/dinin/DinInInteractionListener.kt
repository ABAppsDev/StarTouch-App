package presentation.screen.dinin

interface DinInInteractionListener {
    fun onClickOk()
    fun onClickTable(tableId: Int, tableName: String)
    fun onClickAssignCheck(id: Int)
    fun onClickCheck(id: Long)
    fun onCoversCountChanged(covers: String)
    fun onTableNameChanged(tableName: String)
    fun onDismissDinInDialogue()
    fun showErrorDialogue()
    fun onDismissErrorDialogue()
    fun showErrorScreen()
    fun showWarningDialogue(tableId: Int, tableName: String)
    fun onDismissWarningDialogue()
    fun onConfirmButtonClick()
    fun onLongClick(tableId: Long)
    fun onClickBack()
    fun onClickTableGuest()
    fun onCreateTableGuest()
    fun onClickRoom(id: Int)
    fun onEnterTableName()
}