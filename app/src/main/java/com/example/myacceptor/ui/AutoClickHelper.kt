package com.example.myacceptor.ui

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo

object AutoClickHelper {

    fun findAndClickText(rootNode: AccessibilityNodeInfo?, targetText: String): Boolean {
        if (rootNode == null) return false

        return searchNode(rootNode, targetText)
    }

    private fun searchNode(node: AccessibilityNodeInfo, text: String): Boolean {

        val nodeText = node.text?.toString() ?: ""
        val nodeDesc = node.contentDescription?.toString() ?: ""

        // Match text or content description
        if (nodeText.contains(text, ignoreCase = true) ||
            nodeDesc.contains(text, ignoreCase = true)) {

            if (node.isClickable) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                Log.d("AUTO_CLICK", "Clicked: $text")
                return true
            } else {
                // Try parent
                var parent = node.parent
                while (parent != null) {
                    if (parent.isClickable) {
                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        Log.d("AUTO_CLICK", "Clicked parent of: $text")
                        return true
                    }
                    parent = parent.parent
                }
            }
        }

        // Traverse children
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                val result = searchNode(child, text)
                if (result) return true
            }
        }

        return false
    }
}