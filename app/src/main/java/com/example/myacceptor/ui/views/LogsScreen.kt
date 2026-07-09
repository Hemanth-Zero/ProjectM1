package com.example.myacceptor.ui.views

import androidx.compose.ui.unit.dp

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.myacceptor.AppLogger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogViewerScreen(
    context: Context,
    defaultSearch: String =""
) {
    var logs by remember {
        mutableStateOf(
            AppLogger.readLogs(context)
        )
    }

    var searchText by remember {
        mutableStateOf(defaultSearch)
    }

    val filteredLogs = remember(
        logs,
        searchText
    ) {

        logs.lines()
            .filter {
                it.isNotBlank()
            }
            .filter {
                it.contains(
                    searchText,
                    ignoreCase = true
                )
            }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Column {
                        Text(
                            "Logs",
                            fontWeight =
                                FontWeight.Bold
                        )
                        Text(
                            "${filteredLogs.size} entries",
                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(

                value = searchText,

                onValueChange = {
                    searchText = it
                },

                modifier =
                    Modifier.fillMaxWidth(),

                placeholder = {
                    Text("Search logs...")
                },

                leadingIcon = {

                    Icon(
                        Icons.Default.Search,
                        null
                    )
                },

                singleLine = true
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                FilledTonalButton(
                    onClick = {

                        logs =
                            AppLogger.readLogs(
                                context
                            )
                    }
                ) {

                    Icon(
                        Icons.Default.Refresh,
                        null
                    )

                    Spacer(
                        modifier =
                            Modifier.width(4.dp)
                    )

                    Text("Refresh")
                }

                Button(
                    onClick = {

                        AppLogger.clearLogs(
                            context
                        )

                        logs = ""
                    }
                ) {

                    Icon(
                        Icons.Default.Delete,
                        null
                    )

                    Spacer(
                        modifier =
                            Modifier.width(4.dp)
                    )

                    Text("Clear")
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            if (
                filteredLogs.isEmpty()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        "No logs found"
                    )
                }

            } else {

                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(
                            8.dp
                        )
                ) {

                    itemsIndexed(
                        filteredLogs
                    ) { index, log ->

                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                        ) {

                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            12.dp
                                        )
                            ) {

                                Text(
                                    "#${index + 1}",
                                    style =
                                        MaterialTheme
                                            .typography
                                            .labelMedium
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            4.dp
                                        )
                                )

                                Text(
                                    text = log,
                                    style =
                                        MaterialTheme
                                            .typography
                                            .bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}