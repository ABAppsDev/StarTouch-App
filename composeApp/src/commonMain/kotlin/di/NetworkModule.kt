package di

import data.gateway.remote.authorizationIntercept
import domain.gateway.local.ILocalConfigurationGateway
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import util.getHttpClientEngine

val NetworkModule = module(createdAtStart = true) {
    single(createdAtStart = true) {
        val client = HttpClient(getHttpClientEngine()) {
            expectSuccess = true
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP Client: $message")
                    }
                }
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                header("Content-Type", "application/json")
                //url(get<ILocalConfigurationGateway>().getApiUrl())
                url("http://192.168.1.4:5/")
            }
        }
        authorizationIntercept(client)
        client
    }
}
//41.152.179.215:8888