package ru.fromchat.app.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ru.fromchat.app.FromChatApp

actual fun colorScheme(darkTheme: Boolean): ColorScheme {
    return if (Build.VERSION.SDK_INT >= 31 && dynamicThemeEnabled) {
        if (darkTheme) {
            dynamicDarkColorScheme(FromChatApp.context)
        } else {
            dynamicLightColorScheme(FromChatApp.context)
        }
    } else {
        if (darkTheme) {
            darkColorScheme()
        } else {
            lightColorScheme()
        }
    }
}

@Composable
actual inline fun ProvideContextMenuRepresentation(darkTheme: Boolean, content: @Composable () -> Unit) {
    content()
}

@SuppressLint("ComposableNaming")
@Composable
actual fun fixStatusBar(darkTheme: Boolean, asSystem: Boolean) {
    WindowCompat.getInsetsController(
        LocalActivity.current!!.window,
        LocalView.current
    ).isAppearanceLightStatusBars = !darkTheme
}