package com.example.safelauncher

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat

class SafeLauncherApp : Application() {
    private val globalScreenOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    AppRulesStore.setLockRequired(this@SafeLauncherApp, true)
                }
                Intent.ACTION_SCREEN_ON -> {
                    if (!AppRulesStore.isLockRequired(this@SafeLauncherApp)) return

                    // Try to show launcher lock immediately after wake,
                    // even if user was previously in another app.
                    val lockIntent = Intent(this@SafeLauncherApp, LockScreenActivity::class.java).apply {
                        addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_SINGLE_TOP
                        )
                    }
                    try {
                        startActivity(lockIntent)
                    } catch (_: Throwable) {
                        // If system blocks background launch, HOME entry gate still enforces lock.
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        ContextCompat.registerReceiver(
            this,
            globalScreenOffReceiver,
            IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_OFF)
                addAction(Intent.ACTION_SCREEN_ON)
            },
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }
}
