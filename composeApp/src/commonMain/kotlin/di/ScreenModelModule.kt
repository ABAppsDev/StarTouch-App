package di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.app.AppScreenModel
import presentation.screen.dinin.DinInScreenModel
import presentation.screen.home.HomeScreenModel
import presentation.screen.order.modifiers.ItemModifierState
import presentation.screen.order.modifiers.ItemModifiersListScreenModel
import presentation.screen.setting.SettingScreenModel

val screenModelModule = module {
    factoryOf(::AppScreenModel)
    factoryOf(::HomeScreenModel)
    factoryOf(::DinInScreenModel)
    factoryOf(::PresetsListScreenModel)
    factoryOf(::SettingScreenModel)
    factory { (presetId: Int) ->
        ItemsListScreenModel(get(), presetId)
    }
    factory { (items: List<ItemModifierState>) ->
        ItemModifiersListScreenModel(items)
    }
}