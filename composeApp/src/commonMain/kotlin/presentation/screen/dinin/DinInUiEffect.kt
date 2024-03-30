package presentation.screen.dinin

sealed interface DinInUiEffect {
    data class NavigateToPresetListScreen(val checkId: Long) : DinInUiEffect
}