package di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.app.AppScreenModel
import presentation.screen.dinin.DinInScreenModel
import presentation.screen.home.HomeScreenModel
import presentation.screen.order.OrderScreenModel
import presentation.screen.setting.SettingScreenModel

val screenModelModule = module {
    factoryOf(::AppScreenModel)
    factoryOf(::HomeScreenModel)
    factoryOf(::DinInScreenModel)
    factoryOf(::SettingScreenModel)
    factory { (checkId: Int) ->
        OrderScreenModel(get(), checkId)
    }
}