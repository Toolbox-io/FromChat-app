package ru.fromchat.app.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.fromchat.app.core.config.ServerConfigData

data class ServerConfigState(
    val config: ServerConfigData? = null,
    val isConfigured: Boolean = false
)

object ServerConfigStore {
    private val _state = MutableStateFlow(ServerConfigState())
    val state: StateFlow<ServerConfigState> = _state.asStateFlow()
    
    fun setConfig(config: ServerConfigData) {
        _state.value = ServerConfigState(
            config = config,
            isConfigured = true
        )
    }
    
    fun getConfig(): ServerConfigData? = _state.value.config
}
