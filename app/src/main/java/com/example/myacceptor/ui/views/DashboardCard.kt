package com.example.myacceptor.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardCard(
    title: String,
    value: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Column {

                Text(title)

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(
                    value,
                    style =
                        MaterialTheme
                            .typography
                            .headlineSmall
                )
            }

            icon()
        }
    }
}