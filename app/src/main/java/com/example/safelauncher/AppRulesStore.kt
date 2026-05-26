package com.example.safelauncher

import android.content.Context

object AppRulesStore {
    private const val PREFS_NAME = "safe_launcher_prefs"
    private const val KEY_HIDDEN_FAKE_PACKAGES = "hidden_fake_packages"
    private const val KEY_LOCK_PIN = "lock_pin"
    private const val KEY_SELECTED_BACKGROUND = "selected_background"
    private const val KEY_LOCK_REQUIRED = "lock_required"
    private const val KEY_LAST_SCREEN = "last_screen"

    const val RULES_MENU_PACKAGE = "com.example.safelauncher.RULES_MENU"
    const val DEFAULT_PIN = "1234"
    const val BG_CLASSIC = "classic"
    const val BG_OCEAN = "ocean"
    const val BG_LIGHT = "light"
    const val SCREEN_REAL = "real"
    const val SCREEN_FAKE = "fake"

    fun getHiddenPackages(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_HIDDEN_FAKE_PACKAGES, emptySet())?.toSet() ?: emptySet()
    }

    fun saveHiddenPackages(context: Context, hiddenPackages: Set<String>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(KEY_HIDDEN_FAKE_PACKAGES, hiddenPackages.toSet()).apply()
    }

    fun getLockPin(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LOCK_PIN, DEFAULT_PIN) ?: DEFAULT_PIN
    }

    fun saveLockPin(context: Context, pin: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LOCK_PIN, pin).apply()
    }

    fun getSelectedBackground(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_SELECTED_BACKGROUND, BG_CLASSIC) ?: BG_CLASSIC
    }

    fun saveSelectedBackground(context: Context, background: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SELECTED_BACKGROUND, background).apply()
    }

    fun getSelectedBackgroundRes(context: Context): Int {
        return when (getSelectedBackground(context)) {
            BG_OCEAN -> R.drawable.bg_launcher_wallpaper_ocean
            BG_LIGHT -> R.drawable.bg_launcher_wallpaper_light
            else -> R.drawable.bg_launcher_wallpaper
        }
    }

    fun isLockRequired(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LOCK_REQUIRED, true)
    }

    fun setLockRequired(context: Context, required: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_LOCK_REQUIRED, required).apply()
    }

    fun getLastScreen(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LAST_SCREEN, SCREEN_REAL) ?: SCREEN_REAL
    }

    fun saveLastScreen(context: Context, screen: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LAST_SCREEN, screen).apply()
    }
}
