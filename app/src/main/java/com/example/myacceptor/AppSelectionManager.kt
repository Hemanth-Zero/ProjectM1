package com.example.myacceptor

import android.content.Context

object AppSelectionManager {
    private const val PREF_NAME = "app_prefs"

    private const val KEY_PACKAGE = "selected_package"
    private const val KEY_BUTTON_NAME = "button_name"

    private const val DEFAULT_BUTTON_NAME = "Roll"

    fun saveSelectedApp(context: Context, packageName: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        prefs.edit()
            .putString(KEY_PACKAGE, packageName)
            .apply()
    }

    fun getSelectedApp(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        return prefs.getString(KEY_PACKAGE, null)
    }

    fun setButtonName(context: Context, buttonName: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        prefs.edit()
            .putString(KEY_BUTTON_NAME, buttonName)
            .apply()
    }


    fun getButtonName(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        return prefs.getString(
            KEY_BUTTON_NAME,
            DEFAULT_BUTTON_NAME
        ) ?: DEFAULT_BUTTON_NAME
    }
}