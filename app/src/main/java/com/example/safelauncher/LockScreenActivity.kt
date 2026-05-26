package com.example.safelauncher

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class LockScreenActivity : BaseSecureActivity() {
    private lateinit var passcodeIndicator: TextView
    private val inputBuffer = StringBuilder()
    private var failedAttempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        setContentView(R.layout.activity_lock_screen)

        findViewById<android.view.View>(R.id.lockRoot)
            .setBackgroundResource(AppRulesStore.getSelectedBackgroundRes(this))

        passcodeIndicator = findViewById(R.id.passcodeIndicator)
        setupKeypad()
        updatePasscodeIndicator()
    }

    private fun setupKeypad() {
        val numberButtons = listOf(
            R.id.key0 to "0",
            R.id.key1 to "1",
            R.id.key2 to "2",
            R.id.key3 to "3",
            R.id.key4 to "4",
            R.id.key5 to "5",
            R.id.key6 to "6",
            R.id.key7 to "7",
            R.id.key8 to "8",
            R.id.key9 to "9"
        )

        numberButtons.forEach { (id, number) ->
            findViewById<MaterialButton>(id).setOnClickListener {
                appendNumber(number)
            }
        }

        findViewById<MaterialButton>(R.id.keyDelete).setOnClickListener {
            if (inputBuffer.isNotEmpty()) {
                inputBuffer.deleteCharAt(inputBuffer.length - 1)
                updatePasscodeIndicator()
            }
        }
    }

    private fun appendNumber(number: String) {
        if (inputBuffer.length >= 4) return

        inputBuffer.append(number)
        updatePasscodeIndicator()

        if (inputBuffer.length == 4) {
            checkPin()
        }
    }

    private fun checkPin() {
        if (inputBuffer.toString() == AppRulesStore.getLockPin(this)) {
            failedAttempts = 0
            AppRulesStore.setLockRequired(this, false)
            val target = if (AppRulesStore.getLastScreen(this) == AppRulesStore.SCREEN_FAKE) {
                FakeScreenActivity::class.java
            } else {
                RealScreenActivity::class.java
            }
            startActivity(
                Intent(this, target).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            )
            finish()
            return
        }

        failedAttempts += 1

        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            failedAttempts = 0
            inputBuffer.clear()
            updatePasscodeIndicator()
            AppRulesStore.setLockRequired(this, false)
            AppRulesStore.saveLastScreen(this, AppRulesStore.SCREEN_FAKE)
            startActivity(
                Intent(this, FakeScreenActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            )
            finish()
            return
        }

        inputBuffer.clear()
        updatePasscodeIndicator()
    }

    private fun updatePasscodeIndicator() {
        val symbols = CharArray(4) { index ->
            if (index < inputBuffer.length) '●' else '○'
        }
        passcodeIndicator.text = symbols.joinToString(" ")
    }

    companion object {
        private const val MAX_FAILED_ATTEMPTS = 3
    }
}
