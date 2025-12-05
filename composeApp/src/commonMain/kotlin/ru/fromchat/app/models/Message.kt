package ru.fromchat.app.models

import kotlinx.serialization.Serializable

@Serializable
data class Reaction(
    val emoji: String,
    val count: Int,
    val users: List<ReactionUser> = emptyList()
)

@Serializable
data class ReactionUser(
    val id: Int,
    val username: String
)

@Serializable
data class Attachment(
    val path: String,
    val encrypted: Boolean,
    val name: String
)

@Serializable
data class Message(
    val id: Int,
    val user_id: Int,
    val username: String,
    val content: String,
    val is_read: Boolean,
    val is_edited: Boolean,
    val timestamp: String,
    val profile_picture: String? = null,
    val verified: Boolean? = null,
    val reply_to: Message? = null,
    val files: List<Attachment>? = null,
    val reactions: List<Reaction>? = null
)
