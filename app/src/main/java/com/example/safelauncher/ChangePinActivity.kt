package com.example.safelauncher

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class ChangePinActivity : BaseSecureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pin)

        title = "Ganti PIN"

        val currentPinInput = findViewById<EditText>(R.id.currentPinInput)
        val newPinInput = findViewById<EditText>(R.id.newPinInput)
        val confirmPinInput = findViewById<EditText>(R.id.confirmPinInput)
        val saveButton = findViewById<Button>(R.id.savePinButton)

        saveButton.setOnClickListener {
            val currentPin = currentPinInput.text.toString().trim()
            val newPin = newPinInput.text.toString().trim()
            val confirmPin = confirmPinInput.text.toString().trim()

            if (currentPin.isEmpty() || newPin.isEmpty() || confirmPin.isEmpty()) {
                Toast.makeText(this, "Isi semua kolom PIN", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentPin != AppRulesStore.getLockPin(this)) {
                Toast.makeText(this, "PIN saat ini salah", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPin.length != 4 || !newPin.all { it.isDigit() }) {
                Toast.makeText(this, "PIN baru harus 4 digit angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPin != confirmPin) {
                Toast.makeText(this, "Konfirmasi PIN tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AppRulesStore.saveLockPin(this, newPin)
            Toast.makeText(this, "PIN berhasil diubah", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
