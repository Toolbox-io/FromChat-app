package ru.fromchat.app

import ru.fromchat.app.ui.Theme
import com.pr0gramm3r101.utils.settings.Settings

object Settings {
    val settings = Settings.Companion()

    var materialYou
        get() = settings.getBoolean("materialYou", true)
        set(value) = settings.putBoolean("materialYou", value)

    var theme: Theme
        get() = Theme.entries[settings.getInt("theme", Theme.AsSystem.ordinal)]
        set(value) = settings.putInt("theme", value.ordinal)
}