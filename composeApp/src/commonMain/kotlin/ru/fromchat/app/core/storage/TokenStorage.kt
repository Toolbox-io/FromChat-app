package ru.fromchat.app.core.storage

import com.pr0gramm3r101.utils.storage.AuthStorage

/**
 * Token storage using utils module
 */
object TokenStorage {
    suspend fun getToken(): String? = AuthStorage.getToken()
    suspend fun setToken(token: String?) = AuthStorage.setToken(token)
    suspend fun clearToken() = AuthStorage.clearToken()
}
