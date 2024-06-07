package di

import domain.entity.FireItems
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.app.AppScreenModel
import presentation.screen.dinin.DinInScreenModel
import presentation.screen.home.HomeScreenModel
import presentation.screen.order.OrderScreenModel
import presentation.screen.setting.SettingScreenModel
import presentation.screen.takeaway.order.OrderTakeAwayScreenModel

val screenModelModule = module {
    factoryOf(::AppScreenModel)
    factoryOf(::HomeScreenModel)
    factoryOf(::DinInScreenModel)
    factoryOf(::SettingScreenModel)
    factory { (checkId: Long, items: List<FireItems>, isReopened: Boolean) ->
        OrderScreenModel(get(), get(), get(), checkId, items, isReopened)
    }
    factory { (checkId: Long, items: List<FireItems>, isReopened: Boolean) ->
        OrderTakeAwayScreenModel(get(), get(), get(), checkId, items, isReopened)
    }
}