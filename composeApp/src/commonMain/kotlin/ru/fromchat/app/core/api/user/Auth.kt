package ru.fromchat.app.core.api.user

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.fromchat.app.core.api.HttpClientFactory
import ru.fromchat.app.core.api.HttpException
import ru.fromchat.app.models.CheckAuthResponse
import ru.fromchat.app.models.LoginRequest
import ru.fromchat.app.models.LoginResponse
import ru.fromchat.app.models.RegisterRequest

object Auth {
    private val client: HttpClient = HttpClientFactory.create()
    
    /**
     * Generates authentication headers for API requests
     */
    fun getAuthHeaders(token: String?, json: Boolean = true): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        
        if (json) {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        
        if (token != null) {
            headers[HttpHeaders.Authorization] = "Bearer $token"
        }
        
        return headers
    }
    
    /**
     * Checks if the current user is authenticated
     */
    suspend fun checkAuth(token: String): CheckAuthResponse {
        return try {
            client.get("/check_auth") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            throw HttpException(
                statusCode = 0,
                message = "Failed to check auth",
                detail = e.message
            )
        }
    }
    
    /**
     * Logs in a user with username and password (password should be hashed)
     */
    suspend fun login(request: LoginRequest): LoginResponse {
        return try {
            client.post("/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        } catch (e: Exception) {
            throw HttpException(
                statusCode = 0,
                message = "Failed to login",
                detail = e.message
            )
        }
    }
    
    /**
     * Registers a new user (password should be hashed)
     */
    suspend fun register(request: RegisterRequest): LoginResponse {
        return try {
            client.post("/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        } catch (e: Exception) {
            throw HttpException(
                statusCode = 0,
                message = "Failed to register",
                detail = e.message
            )
        }
    }
    
    /**
     * Logs out the current user
     */
    suspend fun logout(token: String) {
        try {
            client.get("/logout") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        } catch (e: Exception) {
            throw HttpException(
                statusCode = 0,
                message = "Failed to logout",
                detail = e.message
            )
        }
    }
}
