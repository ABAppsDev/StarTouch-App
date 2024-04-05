package presentation.screen.setting

interface SettingInteractionListener {
    fun onClickSave()
    fun onClickClose()
    fun onChooseRest(id: Int)
    fun onChooseOutlet(id: Int)
    fun onChooseDinInRoom(id: Int)
    fun onApiUrlChanged(apiUrl: String)
    fun onWorkStationIdChanged(wsId: String)
    fun onSelectedCallCenter(isSelected: Boolean)
    fun onQuickLoopBackSelected(isSelected: Boolean)
    fun onClickBack()
}