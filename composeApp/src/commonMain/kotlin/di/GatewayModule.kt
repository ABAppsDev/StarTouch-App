package di

import data.gateway.local.LocalConfigurationGateway
import data.gateway.remote.AttendanceGateway
import data.gateway.remote.ChecksGateway
import data.gateway.remote.DinInGateway
import data.gateway.remote.OrderGateway
import data.gateway.remote.PermissionGateway
import data.gateway.remote.SetupGateway
import domain.gateway.IAttendanceGateway
import domain.gateway.IChecksGateway
import domain.gateway.IDinInGateway
import domain.gateway.IOrderGateway
import domain.gateway.IPermissionGateway
import domain.gateway.ISetupGateway
import domain.gateway.local.ILocalConfigurationGateway
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val GatewayModule = module {
    singleOf(::AttendanceGateway) bind IAttendanceGateway::class
    singleOf(::PermissionGateway) bind IPermissionGateway::class
    singleOf(::SetupGateway) bind ISetupGateway::class
    singleOf(::DinInGateway) bind IDinInGateway::class
    singleOf(::OrderGateway) bind IOrderGateway::class
    singleOf(::ChecksGateway) bind IChecksGateway::class
    singleOf(::LocalConfigurationGateway) bind ILocalConfigurationGateway::class
}