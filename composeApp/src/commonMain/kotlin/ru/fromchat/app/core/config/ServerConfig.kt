package ru.fromchat.app.core.config

import com.pr0gramm3r101.utils.storage.ServerConfigData as UtilsServerConfigData
import com.pr0gramm3r101.utils.storage.ServerConfigStorage as UtilsServerConfigStorage

/**
 * Server configuration data
 */
typealias ServerConfigData = UtilsServerConfigData

/**
 * Gets the current server configuration or default
 */
suspend fun getServerConfig(): ServerConfigData {
    return UtilsServerConfigStorage.getConfig()
}

/**
 * Saves server configuration
 */
suspend fun saveServerConfig(config: ServerConfigData) {
    UtilsServerConfigStorage.saveConfig(config)
}

/**
 * Checks if server configuration exists
 */
suspend fun hasServerConfig(): Boolean {
    return UtilsServerConfigStorage.hasConfiguration()
}
