package com.example.myacceptor

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.myacceptor.ui.AutoClickHelper

class ScreenReaderService : AccessibilityService() {

    private val handler = Handler(Looper.getMainLooper())
    private var lastEventTime: Long = 0
    private var lastHandledTime = 0L
    private val EVENT_COOLDOWN = 999L

    override fun onServiceConnected() {
        super.onServiceConnected()
        //startReadingLoop()
    }

    private fun startReadingLoop() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                readScreen()
                handler.postDelayed(this, 30000) // 1 minute
            }
        }, 30000)
    }

    private fun readScreen() {
        val rootNode = rootInActiveWindow ?: return

        val clicked = AutoClickHelper.findAndClickText(rootNode, AppSelectionManager.getButtonName(this))

        if (clicked) {
            Log.d("SERVICE", " button clicked")
        } else {
            Log.d("SERVICE", "button not found")
        }
    }

    private fun extractText(node: AccessibilityNodeInfo, list: MutableList<String>) {
        node.text?.toString()?.let {
            if (it.isNotBlank()) list.add(it)
        }

        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { extractText(it, list) }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val selectedApp = AppSelectionManager.getSelectedApp(this) ?: return
        val eventPackage = event.packageName?.toString() ?: return

        if (eventPackage != selectedApp) return

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastHandledTime < EVENT_COOLDOWN) return
        lastHandledTime = currentTime

        readScreen()
    }
    override fun onInterrupt() {}
}