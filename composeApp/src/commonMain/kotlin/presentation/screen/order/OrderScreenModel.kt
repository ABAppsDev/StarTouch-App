package presentation.screen.order

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseScreenModel

class OrderScreenModel : BaseScreenModel<OrderState, OrderUiEffect>(OrderState()),
    OrderInteractionListener {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    fun retry() {

    }

    override fun onClickPreset(presetId: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickItemModifier(itemId: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickItemChild(itemId: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickItem(itemId: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickFloatActionButton() {
        TODO("Not yet implemented")
    }

    override fun onClickFire() {
        TODO("Not yet implemented")
    }

    override fun onClickClose() {
        TODO("Not yet implemented")
    }

    override fun showErrorScreen() {
        TODO("Not yet implemented")
    }
}