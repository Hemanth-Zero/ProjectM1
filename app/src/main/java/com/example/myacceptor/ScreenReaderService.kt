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
            //Log.d(" Amout Values ", "${ScreenAmout} , ${minAmount} ")
            AppLogger.log(this,"${ScreenAmout} , ${minAmount} ","Detected")

            if(AppSelectionManager.getMoneyOn(this)) {
                if (ScreenAmout > minAmount) {
//                    val AmountClicked =
//                        AutoClickHelper.findAndClickText(
//                            rootNode,
//                            targetText = ScreenAmout.toString()
//                        )
//                    if (!AmountClicked) {
//                        Log.d("Amout", "Amount button not found")
//                        return@postDelayed
//                    }
                    //Log.d("money above"," clicked money button")
                    //AppLogger.log(this,"clicked money button")
                }else{
                    //Log.d("Amout", "Amount is less than limit")
                    AppLogger.log(this,"Amount is less than limit","Amount")
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
                AppLogger.log(this, "button clicked","Accepted")
            } else {
                AppLogger.log(this, "button not found","Accepted")
            }
        }, 5)
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

        val selectedApp = AppSelectionManager.getSelectedApp(this) ?: return
        val eventPackage = event.packageName?.toString() ?: return

        if (eventPackage != selectedApp) return

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastHandledTime < EVENT_COOLDOWN) return
        lastHandledTime = currentTime
        //Log.d( " Event ", "Event is triggered")
        //AppLogger.log(this, "Event is triggered")
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