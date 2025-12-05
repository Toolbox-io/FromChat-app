package ru.fromchat.app.core.api.chats

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.fromchat.app.core.api.HttpClientFactory
import ru.fromchat.app.core.api.HttpException
import ru.fromchat.app.models.Message
import ru.fromchat.app.models.MessagesResponse
import ru.fromchat.app.state.AuthStore

object GeneralChat {
    private val client: HttpClient = HttpClientFactory.create()
    
    /**
     * Fetches public chat messages
     */
    suspend fun fetchMessages(
        token: String,
        limit: Int = 50,
        beforeId: Int? = null
    ): Pair<List<Message>, Boolean> {
        val url = buildString {
            append("/get_messages?limit=$limit")
            if (beforeId != null) {
                append("&before_id=$beforeId")
            }
        }
        
        return try {
            val response: MessagesResponse = client.get(url) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
            
            Pair(
                response.messages,
                response.has_more ?: false
            )
        } catch (e: Exception) {
            throw HttpException(
                statusCode = 0,
                message = "Failed to fetch messages",
                detail = e.message
            )
        }
    }
    
    /**
     * Edits a public chat message
     */
    suspend fun edit(messageId: Int, newContent: String, token: String) {
        try {
            client.put("/edit_message/$messageId") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(mapOf("content" to newContent))
            }
        } catch (e: Exception) {
            throw HttpException(
                statusCode = 0,
                message = "Failed to edit message",
                detail = e.message
            )
        }
    }
    
    /**
     * Deletes a public chat message
     */
    suspend fun deleteMessage(messageId: Int, token: String) {
        try {
            client.delete("/delete_message/$messageId") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        } catch (e: Exception) {
            throw HttpException(
                statusCode = 0,
                message = "Failed to delete message",
                detail = e.message
            )
        }
    }
}
