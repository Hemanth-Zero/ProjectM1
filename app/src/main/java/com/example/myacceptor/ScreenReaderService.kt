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
    }

    private fun readScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val rootNode = rootInActiveWindow ?: return@postDelayed
            val ScreenAmout = getScreenAmount() ?: 0f
            val minAmount =
                AppSelectionManager.getSelectAmount(this) ?: 0f
            //AppLogger.log(this,"${ScreenAmout} , ${minAmount} ","Detected")

            if(AppSelectionManager.getMoneyOn(this)) {
                if (ScreenAmout > minAmount) {
                    //pass
                }else{
                    //AppLogger.log(this,"Amount is less than limit ${ScreenAmout}","Amount")
                    return@postDelayed
                }
            }
            AppLogger.log(this, "ACCEPTED with  ${ScreenAmout} , ${minAmount} " ,"Accepted ")

            val clicked =
                AutoClickHelper.findAndClickText(
                    rootNode,
                    AppSelectionManager.getButtonName(this)
                )
            if (clicked) {
                var temp = AppSelectionManager.getEarnAmount(this) + ScreenAmout

                AppLogger.log(this, "button clicked ${temp}","Confirmed")
                AppSelectionManager.saveEarnAmount(this,temp)
            } else {
                AppLogger.log(this, "button not found","Accepted")
            }
        }, 10)
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
        if(!AppSelectionManager.getAppOn(this)){
            Log.d("IsAppOn","App is off")
            return
        }

        val selectedApp = AppSelectionManager.getSelectedApps(this) ?: return
        val eventPackage = event.packageName?.toString() ?: return

        if (eventPackage !in selectedApp) return

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastHandledTime < EVENT_COOLDOWN) return
        lastHandledTime = currentTime
        readScreen()
    }

    private fun getScreenAmount(): Float? {

        val rootNode = rootInActiveWindow ?: run {
            //Log.d("SCREEN_READ", "No active window")
            return null
        }

        val amount = extractAmount(rootNode)

        if (amount != null) {
            //Log.d("SCREEN_READ", "Detected Amount = ₹$amount")
        } else {
            //Log.d("SCREEN_READ", "No amount found")
        }

        return amount
    }

    private fun extractAmount(node: AccessibilityNodeInfo): Float? {
        val regex = Regex("₹\\s*(\\d+(?:\\.\\d+)?)")
        var foundAmout = 0
        val text = node.text?.toString()
        if (!text.isNullOrBlank()) {
            //Log.d("SCREEN_READ", "TEXT: $text")
            val match = regex.find(text)
            if (match != null) {
                return match.groupValues[1].toFloatOrNull()
            }
        }
        val desc = node.contentDescription?.toString()
        if (!desc.isNullOrBlank()) {
            //Log.d("SCREEN_READ", "DESC: $desc")
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

    override fun onInterrupt() {}
}