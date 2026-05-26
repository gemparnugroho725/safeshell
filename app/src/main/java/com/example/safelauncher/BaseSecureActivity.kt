package com.example.safelauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.PowerManager
import androidx.core.content.ContextCompat

open class BaseSecureActivity : androidx.appcompat.app.AppCompatActivity() {
    private val screenOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_SCREEN_OFF) {
                AppRulesStore.setLockRequired(this@BaseSecureActivity, true)
            }
        }
    }

    private var isReceiverRegistered = false

    override fun onStart() {
        super.onStart()
        if (isReceiverRegistered) return

        ContextCompat.registerReceiver(
            this,
            screenOffReceiver,
            IntentFilter(Intent.ACTION_SCREEN_OFF),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        isReceiverRegistered = true
    }

    override fun onResume() {
        super.onResume()
        if (shouldSkipLockGate()) return
        if (!AppRulesStore.isLockRequired(this)) return

        startActivity(
            Intent(this, LockScreenActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        )
        finish()
    }

    protected open fun shouldSkipLockGate(): Boolean {
        return this is LockScreenActivity || this is HomeEntryActivity
    }

    override fun onPause() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager
        if (powerManager != null && !powerManager.isInteractive) {
            AppRulesStore.setLockRequired(this, true)
        }
        super.onPause()
    }

    override fun onStop() {
        if (isReceiverRegistered) {
            unregisterReceiver(screenOffReceiver)
            isReceiverRegistered = false
        }
        super.onStop()
    }
}
