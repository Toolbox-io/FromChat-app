package ru.fromchat.app.core.crypto

import com.pr0gramm3r101.utils.crypto.deriveAuthSecret

/**
 * Derives authentication secret so the raw password never leaves the client
 * Re-exports from utils module for convenience
 */
suspend fun deriveAuthSecret(username: String, password: String): String {
    return com.pr0gramm3r101.utils.crypto.deriveAuthSecret(username, password)
}