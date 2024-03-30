package presentation.screen.order.presets

interface PresetsInteractionListener {
    fun onClickPreset(presetId: Int)
    fun showErrorScreen()
}