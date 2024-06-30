package presentation.screen.order

interface OrderInteractionListener {
    fun onClickPreset(presetId: Int)
    fun onClickItemModifier(name: String)
    fun onClickItemChild(itemId: Int)
    fun onClickItem(itemId: Int, qty: Float)
    fun onClickFloatActionButton()
    fun onClickFire()
    fun onClickFireAndSettle()
    fun onClickFireAndPrint()
    fun onClickClose()
    fun showErrorScreen()
    fun onClickIconBack()
    fun onClickModifyLastItem(id: Int, serial: Int)
    fun onChooseItem(itemId: Int)
    fun onDismissDialogue()
    fun onModifyLastItemChanged(comment: String)
    fun onPriceChanged(price: String)
    fun onClickOk()
    fun onClickMinus(id: Int)
    fun onClickPlus(id: Int)
    fun onClickRemoveItem(id: Int)
    fun addItem(orderItemState: OrderItemState)
    fun showWarningDialogue()
    fun showPriceDialogue(id: Int, qty: Float)
    fun onClickOkPrice()
    fun onDismissWarningDialogue()
    fun onDismissItemDialogue()
    fun onDismissPriceDialogue()
    fun showWarningItem()
    fun updateTax(tax: Float)
    fun updateAdj(adj: Float)
}