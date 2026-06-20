package com.example.myacceptor.ui.views

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myacceptor.AppSelectionManager

@Composable
fun HomeScreen(
    navController: NavHostController,
    context: Context
) {

    var buttonName by remember {
        mutableStateOf(
            AppSelectionManager
                .getButtonName(context)
        )
    }
    var enabled by remember {
        mutableStateOf(
            AppSelectionManager.getAppOn(context)
        )
    }

    val selectedApps =
        AppSelectionManager.getSelectedApps(context)

    val amount =
        AppSelectionManager.getSelectAmount(context)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        item {

            Text(
                text = "Automation Dashboard",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {

            Card {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text("Automation")

                    Switch(
                        checked = enabled,
                        onCheckedChange = {

                            enabled = it

                            AppSelectionManager
                                .saveAppOn(
                                    context,
                                    it
                                )
                        }
                    )
                }
            }
            Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                Text(
                    "Button Text"
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                OutlinedTextField(

                    value = buttonName,

                    onValueChange = {

                        buttonName = it

                        AppSelectionManager
                            .setButtonName(
                                context,
                                it
                            )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    singleLine = true,

                    placeholder = {
                        Text("Roll")
                    }
                )
            }
        }

        item {

            DashboardCard(
                title = "Apps Selected",
                value = "${selectedApps.size}",
                icon = {
                    Icon(
                        Icons.Default.Apps,
                        null
                    )
                }
            ) {
                navController.navigate(
                    Screen.Apps.route
                )
            }
        }

        item {

            DashboardCard(
                title = "Fare Limit",
                value = "₹${amount.toInt()}",
                icon = {
                    Icon(
                        Icons.Default.Tune,
                        null
                    )
                }
            ) {
                navController.navigate(
                    Screen.Fare.route
                )
            }
        }

        item {

            DashboardCard(
                title = "Logs",
                value = "View",
                icon = {
                    Icon(
                        Icons.Default.List,
                        null
                    )
                }
            ) {
                navController.navigate(
                    Screen.Logs.route
                )
            }
        }
    }
}