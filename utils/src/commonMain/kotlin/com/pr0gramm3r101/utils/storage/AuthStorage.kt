package com.pr0gramm3r101.utils.storage

/**
 * Authentication token storage
 */
object AuthStorage {
    private val storage = NamespacedStorage("auth")
    
    suspend fun getToken(): String? {
        val token = storage.getString("token")
        return if (token.isEmpty()) null else token
    }
    
    suspend fun setToken(token: String?) {
        if (token != null) {
            storage.putString("token", token)
        } else {
            storage.remove("token")
        }
    }
    
    suspend fun clearToken() {
        storage.remove("token")
    }
}
