package presentation.screen.order

interface OrderInteractionListener {
    fun onClickPreset(presetId: Int)
    fun onClickItemModifier(itemId: Int)
    fun onClickItemChild(itemId: Int)
    fun onClickItem(itemId: Int)
    fun onClickFloatActionButton()
    fun onClickFire()
    fun onClickClose()
    fun showErrorScreen()
    fun onClickIconBack()
    fun onClickModifyLastItem()
    fun onDismissDialogue()
    fun onModifyLastItemChanged(comment: String)
    fun onClickOk()
    fun onClickMinus(id:Int)
    fun onClickPlus(id:Int)
    fun onClickRemoveItem(id: Int)
}