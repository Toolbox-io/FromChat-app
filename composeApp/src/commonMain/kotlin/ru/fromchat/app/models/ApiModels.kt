package ru.fromchat.app.models

import kotlinx.serialization.Serializable

// Requests
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val username: String,
    val display_name: String,
    val password: String,
    val confirm_password: String
)

@Serializable
data class SendMessageRequestData(
    val content: String,
    val reply_to_id: Int? = null
)

// Responses
@Serializable
data class LoginResponse(
    val user: User,
    val token: String
)

@Serializable
data class MessagesResponse(
    val status: String? = null,
    val messages: List<Message>,
    val has_more: Boolean? = null
)

@Serializable
data class CheckAuthResponse(
    val authenticated: Boolean,
    val username: String? = null,
    val admin: Boolean? = null
)
