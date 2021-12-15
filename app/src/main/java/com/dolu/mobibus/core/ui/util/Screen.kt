package com.dolu.mobibus.core.ui.util

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
}