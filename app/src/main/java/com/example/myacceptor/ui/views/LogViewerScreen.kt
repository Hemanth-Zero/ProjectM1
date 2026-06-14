package com.example.myacceptor.ui.views

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myacceptor.AppLogger

@Composable
fun LogViewerScreen(
    context: ComponentActivity,
    back:()-> Unit
) {

    var logs by remember {
        mutableStateOf(
            AppLogger.readLogs(context)
        )
    }

    var searchText by remember {
        mutableStateOf("")
    }

    val filteredLogs = remember(logs, searchText) {
        logs.lines().filter {
            it.contains(searchText, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Search logs...")
            },
            singleLine = true
        )
        Row {

            Button(
                onClick = {
                    logs = AppLogger.readLogs(context)
                }
            ) {
                Text("Refresh")
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Button(
                onClick = {

                    AppLogger.clearLogs(context)

                    logs = ""
                }
            ) {
                Text("Clear")
            }
            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Button(
                onClick = {
                    back()
                }
            ) {
                Text("Back")
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredLogs) { log ->

                Text(
                    text = log,
                    style = MaterialTheme.typography.bodyMedium
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(2.dp)
                )
            }
        }
    }
}