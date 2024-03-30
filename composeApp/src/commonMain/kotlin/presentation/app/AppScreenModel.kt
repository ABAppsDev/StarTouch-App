package presentation.app

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.AppLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import presentation.base.BaseScreenModel
import util.LanguageCode

class AppScreenModel : BaseScreenModel<LanguageCode, Nothing>(LanguageCode.EN) {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        getUserLanguageCode()
    }

    private fun getUserLanguageCode() {
        viewModelScope.launch(Dispatchers.IO) {
            AppLanguage.code.collectLatest { lang ->
                updateState {
                    LanguageCode.entries.find { languageCode ->
                        languageCode.value == lang
                    } ?: LanguageCode.EN
                }
            }
        }
    }
}