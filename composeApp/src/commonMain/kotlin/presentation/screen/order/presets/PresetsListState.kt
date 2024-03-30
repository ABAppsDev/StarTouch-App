package presentation.screen.order.presets

import androidx.compose.runtime.Immutable
import data.util.AppLanguage
import domain.entity.Preset
import presentation.base.ErrorState
import util.LanguageCode

@Immutable
data class PresetsListState(
    val errorMessage: String = "",
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val showErrorScreen: Boolean = false,
    val presetItemsState: List<PresetItemState> = emptyList(),
)

@Immutable
data class PresetItemState(
    val id: Int = 0,
    val name: String = "",
)


fun Preset.toPresetItemState(): PresetItemState = PresetItemState(
    id = id,
    name = if (AppLanguage.code.value == LanguageCode.EN.value) name else name2,
)