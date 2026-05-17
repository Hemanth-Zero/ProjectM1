package com.example.myacceptor

import android.content.Context

object AppSelectionManager {
    private const val PREF_NAME = "app_prefs"

    private const val KEY_PACKAGE = "selected_package"
    private const val KEY_BUTTON_NAME = "button_name"
    private const val KEY_AMOUNT = "key_amount"
    private const val DEFAULT_BUTTON_NAME = "Roll"

    fun saveSelectedApp(context: Context, packageName: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        prefs.edit()
            .putString(KEY_PACKAGE, packageName)
            .apply()
    }
    fun saveSelectAmount(context: Context,pref_amount: Float){
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        prefs.edit()
            .putFloat(KEY_AMOUNT,pref_amount)
            .apply()

    }
    fun getSelectAmount(context: Context): Float?{
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_AMOUNT,0f)
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