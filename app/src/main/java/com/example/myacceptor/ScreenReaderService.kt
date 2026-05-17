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
    private val EVENT_COOLDOWN = 200L

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
        Handler(Looper.getMainLooper()).postDelayed({
            val rootNode = rootInActiveWindow ?:return@postDelayed

            val ScreenAmout = getScreenAmount() ?: 0f
            val minAmount =
                AppSelectionManager.getSelectAmount(this) ?: 0f

            if (ScreenAmout < minAmount) {
                return@postDelayed
            }
            val AmountClicked = AutoClickHelper.findAndClickText(rootNode, targetText = ScreenAmout.toString())

            if(!AmountClicked){
                Log.d("Amout","Amount button not found")
                return@postDelayed
            }
            val clicked =
                AutoClickHelper.findAndClickText(rootNode, AppSelectionManager.getButtonName(this))
            if (clicked) {
                Log.d("SERVICE", " button clicked")
            } else {
                Log.d("SERVICE", "button not found")
            }
        }, 250)
    }

    private fun extractText(node: AccessibilityNodeInfo, list: MutableList<String>) {
        node.text?.toString()?.let {
            if (it.isNotBlank()) list.add(it)
        }

        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { extractText(it, list) }
        }
    }
    //Exit the app Go back to home screen

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val selectedApp = AppSelectionManager.getSelectedApp(this) ?: return
        val eventPackage = event.packageName?.toString() ?: return

        if (eventPackage != selectedApp) return

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastHandledTime < EVENT_COOLDOWN) return
        lastHandledTime = currentTime
        Log.d("Event ", "Event is triggered")
        //printEntireScreen()
        //readScreen()
    }

    private fun getScreenAmount(): Float? {

        val rootNode = rootInActiveWindow ?: run {
            Log.d("SCREEN_READ", "No active window")
            return null
        }

        val amount = extractAmount(rootNode)

        if (amount != null) {
            Log.d("SCREEN_READ", "Detected Amount = ₹$amount")
        } else {
            Log.d("SCREEN_READ", "No amount found")
        }

        return amount
    }

    private fun extractAmount(node: AccessibilityNodeInfo): Float? {
        val regex = Regex("₹\\s*(\\d+(?:\\.\\d+)?)")

        val text = node.text?.toString()
        if (!text.isNullOrBlank()) {
            Log.d("SCREEN_READ", "TEXT: $text")
            val match = regex.find(text)
            if (match != null) {
                return match.groupValues[1].toFloatOrNull()
            }
        }

        val desc = node.contentDescription?.toString()
        if (!desc.isNullOrBlank()) {
            Log.d("SCREEN_READ", "DESC: $desc")
            val match = regex.find(desc)
            if (match != null) {
                return match.groupValues[1].toFloatOrNull()
            }
        }
        for (i in 0 until node.childCount) {

            val child = node.getChild(i) ?: continue

            val result = extractAmount(child)

            if (result != null) {
                return result
            }
        }

        return null
    }
//    private fun printEntireScreen() {
//
//        val rootNode = rootInActiveWindow ?: run {
//            Log.d("SCREEN_READ", "No active window")
//            return
//        }
//
//        val allText = mutableListOf<String>()
//
//        extractAllText(rootNode, allText)
//
//        Log.d(
//            "SCREEN_READ",
//            "\n================ SCREEN CONTENT ================\n" +
//                    allText.joinToString("\n") +
//                    "\n================================================"
//        )
//    }
//    private fun extractAllText(
//        node: AccessibilityNodeInfo,
//        list: MutableList<String>
//    ) {
//
//        val text = node.text?.toString()
//        val desc = node.contentDescription?.toString()
//
//        if (!text.isNullOrBlank()) {
//            list.add("TEXT: $text")
//        }
//
//        if (!desc.isNullOrBlank()) {
//            list.add("DESC: $desc")
//        }
//
//        for (i in 0 until node.childCount) {
//            node.getChild(i)?.let {
//                extractAllText(it, list)
//            }
//        }
//    }
    override fun onInterrupt() {}
}