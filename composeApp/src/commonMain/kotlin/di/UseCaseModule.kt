package di

import domain.usecase.AdminSystemUseCase
import domain.usecase.ControlPermissionUseCase
import domain.usecase.GetAppSetupUseCase
import domain.usecase.GetRestaurantTablesUseCase
import domain.usecase.ManageAttendanceUseCase
import domain.usecase.ManageChecksUseCase
import domain.usecase.ManageDinInUseCase
import domain.usecase.ManageOrderUseCase
import domain.usecase.ManageSettingUseCase
import domain.usecase.ValidationAuthUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val UseCaseModule = module {
    singleOf(::ManageAttendanceUseCase)
    singleOf(::GetAppSetupUseCase)
    singleOf(::ValidationAuthUseCase)
    singleOf(::ControlPermissionUseCase)
    singleOf(::GetRestaurantTablesUseCase)
    singleOf(::ManageOrderUseCase)
    singleOf(::ManageSettingUseCase)
    singleOf(::AdminSystemUseCase)
    singleOf(::ManageChecksUseCase)
    singleOf(::ManageDinInUseCase)
}