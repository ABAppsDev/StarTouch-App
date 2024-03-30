package presentation.screen.dinin

interface DinInInteractionListener {
    fun onClickOk()
    fun onClickTable(tableId: Int, tableName: Int)
    fun onClickAssignDrawer(id:Int)
    fun onCoversCountChanged(covers: String)
    fun onDismissDinInDialogue()
    fun showErrorDialogue()
    fun onDismissErrorDialogue()
    fun showErrorScreen()
    fun showWarningDialogue(tableId: Int, tableName: Int)
    fun onDismissWarningDialogue()
    fun onConfirmButtonClick()
}