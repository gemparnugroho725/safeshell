package com.example.safelauncher

import android.content.Intent
import android.os.Bundle

class HomeEntryActivity : BaseSecureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val target = when {
            AppRulesStore.isLockRequired(this) -> LockScreenActivity::class.java
            AppRulesStore.getLastScreen(this) == AppRulesStore.SCREEN_FAKE -> FakeScreenActivity::class.java
            else -> RealScreenActivity::class.java
        }

        startActivity(
            Intent(this, target).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        )
        finish()
    }
}
