package com.example.myacceptor.ui.views

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import com.example.myacceptor.AppSelectionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(
    navController: NavHostController,
    context: Context
) {

    val activity =
        context as ComponentActivity

    val pm =
        activity.packageManager

    var search by remember {
        mutableStateOf("")
    }

    var selectedApps by remember {
        mutableStateOf(
            AppSelectionManager
                .getSelectedApps(context)
        )
    }

    val apps = remember {

        pm.getInstalledApplications(
            PackageManager.GET_META_DATA
        )
            .filter {
                pm.getLaunchIntentForPackage(
                    it.packageName
                ) != null
            }
            .sortedBy {
                pm.getApplicationLabel(it)
                    .toString()
            }
    }

    val filteredApps =
        apps.filter {

            pm.getApplicationLabel(it)
                .toString()
                .contains(
                    search,
                    true
                )
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
            },

            modifier =
                Modifier.fillMaxWidth(),

            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    null
                )
            },

            label = {
                Text("Search Apps")
            }
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        LazyColumn {

            items(filteredApps) { app ->

                val packageName =
                    app.packageName

                val appName =
                    pm.getApplicationLabel(app)
                        .toString()

                val selected =
                    selectedApps.contains(
                        packageName
                    )

                val iconBitmap =
                    remember(packageName) {

                        pm.getApplicationIcon(
                            packageName
                        ).toBitmap(
                            128,
                            128
                        )
                    }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Image(
                            bitmap =
                                iconBitmap
                                    .asImageBitmap(),

                            contentDescription =
                                null,

                            modifier = Modifier
                                .size(48.dp)
                                .clip(
                                    RoundedCornerShape(
                                        12.dp
                                    )
                                )
                        )

                        Spacer(
                            modifier =
                                Modifier.width(12.dp)
                        )

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(appName)

                            Text(
                                packageName,
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodySmall
                            )
                        }

                        Switch(
                            checked = selected,

                            onCheckedChange = {

                                AppSelectionManager
                                    .toggleApp(
                                        context,
                                        packageName
                                    )

                                selectedApps =
                                    AppSelectionManager
                                        .getSelectedApps(
                                            context
                                        )
                            }
                        )
                    }
                }
            }
        }
    }
}