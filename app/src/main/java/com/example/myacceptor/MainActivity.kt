package com.example.myacceptor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myacceptor.ui.theme.MyAcceptorTheme
import com.example.myacceptor.ui.views.AppNavHost
import com.example.myacceptor.ui.views.BottomNavigationBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (!isAccessibilityEnabled()) {
                openAccessibilitySettings()
            }
            MyAcceptorTheme {
                MainContainer()
            }
        }
    }
    private fun openAccessibilitySettings() {
        val intent = Intent(
            Settings.ACTION_ACCESSIBILITY_SETTINGS
        )
        startActivity(intent)
    }
    private fun isAccessibilityEnabled(): Boolean {

        val accessibilityManager =
            getSystemService(ACCESSIBILITY_SERVICE)
                    as AccessibilityManager

        val enabledServices =
            accessibilityManager
                .getEnabledAccessibilityServiceList(
                    AccessibilityServiceInfo.FEEDBACK_ALL_MASK
                )

        for (service in enabledServices) {

            val enabledService =
                service.resolveInfo.serviceInfo

            val componentName = ComponentName(
                this,
                ScreenReaderService::class.java
            )

            if (
                enabledService.packageName ==
                componentName.packageName &&
                enabledService.name ==
                componentName.className
            ) {
                return true
            }
        }

        return false
    }
}
@Composable
fun MainContainer() {
    val navController =
        androidx.navigation.compose.rememberNavController()

    androidx.compose.material3.Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController
            )
        }
    ) { padding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AppNavHost(
                navController
            )
        }
    }
}