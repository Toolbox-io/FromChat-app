package ru.fromchat.app.core.websocket

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.fromchat.app.core.config.Config
import ru.fromchat.app.models.Message
import ru.fromchat.app.state.AuthStore

@Serializable
data class WebSocketMessage<T>(
    val type: String,
    val credentials: WebSocketCredentials? = null,
    val data: T? = null,
    val error: WebSocketError? = null
)

@Serializable
data class WebSocketCredentials(
    val scheme: String,
    val credentials: String
)

@Serializable
data class WebSocketError(
    val code: Int,
    val detail: String
)

@Serializable
data class SendMessageData(
    val content: String,
    val reply_to_id: Int? = null
)

@Serializable
data class NewMessageData(
    val id: Int,
    val user_id: Int,
    val username: String,
    val content: String,
    val is_read: Boolean,
    val is_edited: Boolean,
    val timestamp: String,
    val profile_picture: String? = null,
    val verified: Boolean? = null
)

object WebSocketManager {
    private var session: DefaultWebSocketSession? = null
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    
    private val _messages = MutableSharedFlow<Message>()
    val messages: SharedFlow<Message> = _messages.asSharedFlow()
    
    private var reconnectJob: Job? = null
    private var reconnectAttempts = 0
    private val maxReconnectDelay = 30000L
    private val initialReconnectDelay = 1000L
    
    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    suspend fun connect() {
        if (session != null && _isConnected.value) {
            return
        }
        
        reconnectJob?.cancel()
        reconnectAttempts = 0
        
        try {
            val token = AuthStore.getToken()
            val url = Config.getWebSocketUrl()
            
            client.webSocket(url) {
                session = this
                _isConnected.value = true
                reconnectAttempts = 0
                
                // Authenticate if we have a token
                if (token != null) {
                    send(
                        WebSocketMessage(
                            type = "ping",
                            credentials = WebSocketCredentials("Bearer", token),
                            data = null
                        )
                    )
                }
                
                // Start receiving messages
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        handleMessage(frame.readText())
                    }
                }
            }
        } catch (e: Exception) {
            _isConnected.value = false
            session = null
            scheduleReconnect()
        }
    }
    
    private fun handleMessage(text: String) {
        try {
            val json = Json { ignoreUnknownKeys = true }
            val message = json.decodeFromString<WebSocketMessage<*>>(text)
            
            when (message.type) {
                "newMessage" -> {
                    val data = json.decodeFromString<NewMessageData>(
                        json.encodeToString(message.data)
                    )
                    val msg = Message(
                        id = data.id,
                        user_id = data.user_id,
                        username = data.username,
                        content = data.content,
                        is_read = data.is_read,
                        is_edited = data.is_edited,
                        timestamp = data.timestamp,
                        profile_picture = data.profile_picture,
                        verified = data.verified
                    )
                    CoroutineScope(Dispatchers.Default).launch {
                        _messages.emit(msg)
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore parsing errors
        }
    }
    
    suspend fun sendMessage(content: String, replyToId: Int? = null) {
        val token = AuthStore.getToken() ?: return
        
        val message = WebSocketMessage(
            type = "sendMessage",
            credentials = WebSocketCredentials("Bearer", token),
            data = SendMessageData(content.trim(), replyToId)
        )
        
        try {
            session?.send(Json.encodeToString(WebSocketMessage.serializer(), message))
        } catch (e: Exception) {
            // Handle send error
        }
    }
    
    private fun scheduleReconnect() {
        reconnectJob?.cancel()
        reconnectJob = CoroutineScope(Dispatchers.Default).launch {
            val delay = minOf(
                initialReconnectDelay * (1 shl reconnectAttempts),
                maxReconnectDelay
            )
            reconnectAttempts++
            delay(delay)
            connect()
        }
    }
    
    suspend fun disconnect() {
        reconnectJob?.cancel()
        session?.close()
        session = null
        _isConnected.value = false
    }
}
