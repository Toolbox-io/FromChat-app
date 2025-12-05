package ru.fromchat.app.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.fromchat.app.models.User

data class AuthState(
    val currentUser: User? = null,
    val authToken: String? = null,
    val isAuthenticated: Boolean = false
)

object AuthStore {
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()
    
    fun setUser(token: String, user: User) {
        _state.value = AuthState(
            currentUser = user,
            authToken = token,
            isAuthenticated = true
        )
    }
    
    fun logout() {
        _state.value = AuthState()
    }
    
    fun getToken(): String? = _state.value.authToken
}
