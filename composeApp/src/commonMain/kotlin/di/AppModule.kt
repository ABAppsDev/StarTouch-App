package di

import org.koin.dsl.module

val AppModule = module(createdAtStart = true) {
    includes(NetworkModule, screenModelModule, GatewayModule, UseCaseModule, localStorageModule)
}