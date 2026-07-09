package com.example.myacceptor

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppLogger {

    private const val FILE_NAME = "app_log.txt"

    fun log(
        context: Context,
        message: String,
        type:String
    ) {

        val time = SimpleDateFormat(
            "HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        val line = "$type [$time] $message\n"

        context.openFileOutput(
            FILE_NAME,
            Context.MODE_APPEND
        ).use {
            it.write(line.toByteArray())
        }
    }

    fun readLogs(context: Context): String {

        return try {

            context.openFileInput(FILE_NAME)
                .bufferedReader()
                .use {
                    it.readText()
                }

        } catch (_: Exception) {
            ""
        }
    }

    fun clearLogs(context: Context) {
        AppSelectionManager.saveEarnAmount(context,0f)
        context.deleteFile(FILE_NAME)
    }
}