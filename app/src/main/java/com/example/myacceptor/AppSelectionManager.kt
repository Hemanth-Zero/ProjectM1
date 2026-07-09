package com.example.myacceptor

import android.content.Context

object AppSelectionManager {

    private const val PREF_NAME = "app_prefs"

    private const val KEY_PACKAGES = "selected_packages"

    private const val KEY_BUTTON_NAME = "button_name"

    private const val KEY_AMOUNT = "key_amount"

    private const val EARN_AMOUNT = "earn_amount"

    private const val APP_ON = "app_on"

    private const val MONEY_ON = "money_on"

    private const val DEFAULT_BUTTON_NAME = "Roll"

    fun saveAppOn(
        context: Context,
        isAppOn: Boolean
    ) {
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putBoolean(APP_ON, isAppOn)
            .apply()
    }

    fun getAppOn(
        context: Context
    ): Boolean {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getBoolean(
                APP_ON,
                false
            )
    }

    fun saveMoneyOn(
        context: Context,
        isMoneyOn: Boolean
    ) {
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putBoolean(
                MONEY_ON,
                isMoneyOn
            )
            .apply()
    }

    fun getMoneyOn(
        context: Context
    ): Boolean {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getBoolean(
                MONEY_ON,
                false
            )
    }

    fun saveSelectedApps(
        context: Context,
        packages: Set<String>
    ) {

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putStringSet(
                KEY_PACKAGES,
                packages
            )
            .apply()
    }

    fun getSelectedApps(
        context: Context
    ): MutableSet<String> {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getStringSet(
                KEY_PACKAGES,
                emptySet()
            )
            ?.toMutableSet()
            ?: mutableSetOf()
    }

    fun toggleApp(
        context: Context,
        packageName: String
    ) {

        val selected =
            getSelectedApps(context)

        if (selected.contains(packageName)) {
            selected.remove(packageName)
        } else {
            selected.add(packageName)
        }

        saveSelectedApps(
            context,
            selected
        )
    }

    fun saveSelectAmount(
        context: Context,
        amount: Float
    ) {

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putFloat(
                KEY_AMOUNT,
                amount
            )
            .apply()
    }

    fun getSelectAmount(
        context: Context
    ): Float {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getFloat(
                KEY_AMOUNT,
                0f
            )
    }

    fun saveEarnAmount(
        context: Context,
        amount: Float
    ) {

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putFloat(
                EARN_AMOUNT,
                amount
            )
            .apply()
    }

    fun getEarnAmount(
        context: Context
    ): Float {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getFloat(
                EARN_AMOUNT,
                0f
            )
    }

    fun setButtonName(
        context: Context,
        buttonName: String
    ) {

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putString(
                KEY_BUTTON_NAME,
                buttonName
            )
            .apply()
    }

    fun getButtonName(
        context: Context
    ): String {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getString(
                KEY_BUTTON_NAME,
                DEFAULT_BUTTON_NAME
            )
            ?: DEFAULT_BUTTON_NAME
    }
}