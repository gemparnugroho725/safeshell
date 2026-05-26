package com.example.safelauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_SCREEN_OFF) {
            AppRulesStore.setLockRequired(context, true)
        }
    }
}
