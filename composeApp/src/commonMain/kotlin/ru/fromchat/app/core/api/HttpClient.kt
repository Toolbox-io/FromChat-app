package ru.fromchat.app.core.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.fromchat.app.core.config.Config
import ru.fromchat.app.state.AuthStore

/**
 * Creates and configures the HTTP client
 */
expect fun createHttpClient(): HttpClient

/**
 * HTTP client factory
 */
object HttpClientFactory {
    fun create(): HttpClient {
        val client = createHttpClient()
        
        client.config {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = false
                })
            }
            
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
            
            defaultRequest {
                url(Config.getApiBaseUrl())
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                
                val token = AuthStore.getToken()
                if (token != null) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
        
        return client
    }
}

/**
 * HttpException for API errors
 */
class HttpException(
    val statusCode: Int,
    message: String,
    val detail: String? = null
) : Exception(message)
