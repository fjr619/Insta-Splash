package com.fjr619.instasplash.data.remote

import com.fjr619.instasplash.BuildConfig
import com.fjr619.instasplash.data.remote.response.FailedResponse
import com.fjr619.instasplash.data.remote.response.FailedResponseException
import com.fjr619.instasplash.data.util.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(httpClientEngine: HttpClientEngine) = HttpClient(httpClientEngine) {
    expectSuccess = true
    install(HttpCache)
    install(Resources)
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }

    install(Logging) {
        level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
            }
        }
    }

    HttpResponseValidator {
        //ketika ada error request non 200 akan di handle disini
        handleResponseExceptionWithRequest { exception, _ ->
            when(exception) {
                is ResponseException -> {
                    val responseException = exception.response.body<FailedResponse>()
                    throw FailedResponseException(
                        message = responseException.error
                    )
                }
            }
        }
    }

    defaultRequest {
        url(Constants.BASE_URL)
        header("Authorization", "Client-ID ${BuildConfig.API_KEY}")
        contentType(ContentType.Application.Json)
    }
}