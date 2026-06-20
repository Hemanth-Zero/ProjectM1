package com.example.myacceptor.ui.views

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myacceptor.AppSelectionManager

@Composable
fun FareLimitScreen(
    navController: NavHostController,
    context: Context
) {

    var amount by remember {
        mutableFloatStateOf(
            AppSelectionManager
                .getSelectAmount(context)
        )
    }

    var amountText by remember {
        mutableStateOf(
            amount.toInt().toString()
        )
    }

    var moneyEnabled by remember {
        mutableStateOf(
            AppSelectionManager
                .getMoneyOn(context)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Fare Limit",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Card {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(
                    "Enable Fare Filter"
                )

                Switch(
                    checked = moneyEnabled,

                    onCheckedChange = {

                        moneyEnabled = it

                        AppSelectionManager
                            .saveMoneyOn(
                                context,
                                it
                            )
                    }
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Text(
            text = "₹${amount.toInt()}",
            style =
                MaterialTheme
                    .typography
                    .displaySmall
        )

        Slider(
            value = amount,

            onValueChange = {

                amount = it

                amountText =
                    it.toInt().toString()
            },

            valueRange = 0f..5000f
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        OutlinedTextField(

            value = amountText,

            onValueChange = {

                amountText = it

                val parsed =
                    it.toFloatOrNull()

                if (parsed != null) {
                    amount = parsed
                }
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                Text(
                    "Amount"
                )
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Number
                )
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Button(
            onClick = {

                AppSelectionManager
                    .saveSelectAmount(
                        context,
                        amount
                    )
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Save")
        }
    }
}