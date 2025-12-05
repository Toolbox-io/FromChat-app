package ru.fromchat.app.core.api

import ru.fromchat.app.core.api.chats.GeneralChat
import ru.fromchat.app.core.api.user.Auth

/**
 * Main API object with nested groups matching web client structure
 */
object Api {
    val chats = Chats
    val user = User
    
    object Chats {
        val general = GeneralChat
    }
    
    object User {
        val auth = Auth
    }
}
