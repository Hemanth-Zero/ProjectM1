//package com.example.myacceptor.ui.views
//
//import android.content.pm.ApplicationInfo
//import android.content.pm.PackageManager
//import androidx.activity.ComponentActivity
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.core.graphics.drawable.toBitmap
//import com.example.myacceptor.AppSelectionManager
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppSelectorScreen(context: ComponentActivity) {
//
//    val pm = context.packageManager
//
//    val apps = remember {
//        pm.getInstalledApplications(PackageManager.GET_META_DATA)
//            .filter {
//                pm.getLaunchIntentForPackage(it.packageName) != null
//            }
//            .sortedBy {
//                pm.getApplicationLabel(it).toString()
//            }
//    }
//
//    var selectedApp by remember {
//        mutableStateOf(
//            AppSelectionManager.getSelectedApp(context)
//        )
//    }
//
//    var searchQuery by remember {
//        mutableStateOf("")
//    }
//
//    var buttonName by remember {
//        mutableStateOf(
//            AppSelectionManager.getButtonName(context)
//        )
//    }
//
//    var useAmountFilter by remember {
//        mutableStateOf(
//            AppSelectionManager.getAppOn(context)
//        )
//    }
//
//    var amountText by remember {
//        mutableStateOf(
//            AppSelectionManager
//                .getSelectAmount(context)
//                ?.toString() ?: ""
//        )
//    }
//
//    val filteredApps = remember(searchQuery) {
//        apps.filter {
//            pm.getApplicationLabel(it)
//                .toString()
//                .contains(searchQuery, ignoreCase = true)
//        }
//    }
//
//    var appEnabled by remember {
//        mutableStateOf(
//            AppSelectionManager.getAppOn(context)
//        )
//    }
//
//    var showLogs by remember {
//        mutableStateOf(false)
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        "Select Application",
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                actions = {
//                    Button(
//                        onClick = {
//                            showLogs = true
//                        },
//                        modifier = Modifier,
//                        shape = RoundedCornerShape(16.dp)
//                    ) {
//                        Text("View Logs")
//                    }
//                }
//            )
//        }
//
//    ) { padding ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(16.dp)
//        ) {
//            if (showLogs) {
//
//                LogViewerScreen(context, back = {showLogs = false})
//
//                Spacer(
//                    modifier = Modifier.height(12.dp)
//                )
//
//                Button(
//                    onClick = {
//                        showLogs = false
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Back To Settings")
//                }
//
//                return@Column
//            }
//            // ---------------- APP ENABLE SWITCH ----------------
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//                Text(
//                    text = "Automation Enabled",
//                    style = MaterialTheme.typography.bodyLarge
//                )
//
//                Switch(
//                    checked = appEnabled,
//                    onCheckedChange = {
//
//                        appEnabled = it
//
//                        AppSelectionManager.saveAppOn(
//                            context,
//                            it
//                        )
//                    }
//                )
//            }
//            // ---------------- SEARCH ----------------
//
//            OutlinedTextField(
//                value = searchQuery,
//                onValueChange = { searchQuery = it },
//                modifier = Modifier.fillMaxWidth(),
//                placeholder = {
//                    Text("Search apps...")
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = null
//                    )
//                },
//                singleLine = true,
//                shape = RoundedCornerShape(16.dp)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // ---------------- SETTINGS TITLE ----------------
//
//            Text(
//                text = "Automation Settings",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // ---------------- BUTTON TEXT ----------------
//
//            OutlinedTextField(
//                value = buttonName,
//                onValueChange = {
//                    buttonName = it
//                },
//                modifier = Modifier.fillMaxWidth(),
//                placeholder = {
//                    Text("Example: Roll")
//                },
//                label = {
//                    Text("Button Text")
//                },
//                singleLine = true,
//                shape = RoundedCornerShape(16.dp)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // ---------------- AMOUNT SWITCH ----------------
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//                Text(
//                    text = "Use Amount Filter",
//                    style = MaterialTheme.typography.bodyLarge
//                )
//
//                Switch(
//                    checked = useAmountFilter,
//                    onCheckedChange = {
//                        useAmountFilter = it
//                        AppSelectionManager.saveMoneyOn(
//                            context,
//                            it
//                        )
//                    }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // ---------------- AMOUNT FIELD ----------------
//
//            if (useAmountFilter) {
//
//                OutlinedTextField(
//                    value = amountText,
//                    onValueChange = {
//                        amountText = it
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    placeholder = {
//                        Text("Enter minimum amount")
//                    },
//                    label = {
//                        Text("Amount")
//                    },
//                    singleLine = true,
//                    shape = RoundedCornerShape(16.dp)
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // ---------------- SAVE BUTTON ----------------
//
//            Button(
//                onClick = {
//
//                    AppSelectionManager.setButtonName(
//                        context,
//                        buttonName
//                    )
//
//                    if (amountText.isNotBlank()) {
//
//                        AppSelectionManager.saveSelectAmount(
//                            context,
//                            amountText.toFloatOrNull() ?: 0f
//                        )
//                    }
//                },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Text("Save Automation Settings")
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // ---------------- APPS TITLE ----------------
//
//            Text(
//                text = "Installed Apps (${filteredApps.size})",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // ---------------- APP LIST ----------------
//
//            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(10.dp)
//            ) {
//
//                items(filteredApps) { app ->
//
//                    val appName =
//                        pm.getApplicationLabel(app).toString()
//
//                    val iconBitmap = remember(app.packageName) {
//                        pm.getApplicationIcon(app.packageName)
//                            .toBitmap(128, 128)
//                    }
//
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//
//                                selectedApp = app.packageName
//
//                                AppSelectionManager.saveSelectedApp(
//                                    context,
//                                    app.packageName
//                                )
//                            },
//                        shape = RoundedCornerShape(18.dp),
//                        elevation = CardDefaults.cardElevation(4.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor =
//                                if (selectedApp == app.packageName)
//                                    MaterialTheme.colorScheme.primaryContainer
//                                else
//                                    MaterialTheme.colorScheme.surface
//                        )
//                    ) {
//
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(14.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//
//                            Image(
//                                bitmap = iconBitmap.asImageBitmap(),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(52.dp)
//                                    .clip(RoundedCornerShape(14.dp))
//                            )
//
//                            Spacer(
//                                modifier = Modifier.width(14.dp)
//                            )
//
//                            Column(
//                                modifier = Modifier.weight(1f)
//                            ) {
//
//                                Text(
//                                    text = appName,
//                                    style = MaterialTheme.typography.titleMedium,
//                                    fontWeight = FontWeight.Medium
//                                )
//
//                                Spacer(
//                                    modifier = Modifier.height(2.dp)
//                                )
//
//                                Text(
//                                    text = app.packageName,
//                                    style = MaterialTheme.typography.bodySmall,
//                                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                                )
//                            }
//
//                            if (selectedApp == app.packageName) {
//
//                                Icon(
//                                    imageVector = Icons.Default.CheckCircle,
//                                    contentDescription = null,
//                                    tint = MaterialTheme.colorScheme.primary,
//                                    modifier = Modifier.size(28.dp)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}