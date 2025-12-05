package ru.fromchat.app.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val created_at: String,
    val last_seen: String,
    val online: Boolean,
    val username: String,
    val display_name: String,
    val admin: Boolean? = null,
    val bio: String? = null,
    val profile_picture: String,
    val verified: Boolean? = null,
    val suspended: Boolean? = null,
    val suspension_reason: String? = null,
    val deleted: Boolean? = null
)
