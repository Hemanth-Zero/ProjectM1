package com.example.myacceptor.ui.views

import androidx.compose.ui.unit.dp

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun LogsScreen(
    navController: NavHostController,
    context: Context
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Text(
            text = "Logs",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium,
            modifier =
                Modifier.padding(16.dp)
        )

        LogViewerScreen(
            context = context,
            back = {}
        )
    }
}