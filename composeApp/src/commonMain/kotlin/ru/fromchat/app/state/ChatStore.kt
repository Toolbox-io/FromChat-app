package ru.fromchat.app.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.fromchat.app.models.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = false
)

object ChatStore {
    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()
    
    fun addMessage(message: Message) {
        val currentMessages = _state.value.messages
        if (currentMessages.any { it.id == message.id }) {
            return // Message already exists
        }
        _state.value = _state.value.copy(
            messages = currentMessages + message
        )
    }
    
    fun setMessages(messages: List<Message>) {
        _state.value = _state.value.copy(
            messages = messages,
            isLoading = false,
            error = null
        )
    }
    
    fun updateMessage(messageId: Int, updatedMessage: Message) {
        _state.value = _state.value.copy(
            messages = _state.value.messages.map { msg ->
                if (msg.id == messageId) updatedMessage else msg
            }
        )
    }
    
    fun removeMessage(messageId: Int) {
        _state.value = _state.value.copy(
            messages = _state.value.messages.filter { it.id != messageId }
        )
    }
    
    fun setLoading(loading: Boolean) {
        _state.value = _state.value.copy(isLoading = loading)
    }
    
    fun setError(error: String?) {
        _state.value = _state.value.copy(error = error, isLoading = false)
    }
    
    fun setHasMore(hasMore: Boolean) {
        _state.value = _state.value.copy(hasMore = hasMore)
    }
    
    fun clearMessages() {
        _state.value = _state.value.copy(messages = emptyList())
    }
}
