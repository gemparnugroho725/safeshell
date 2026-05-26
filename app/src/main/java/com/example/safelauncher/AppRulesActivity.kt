package com.example.safelauncher

import android.content.Intent
import android.os.Bundle

import com.google.android.material.card.MaterialCardView

class AppRulesActivity : BaseSecureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_rules)

        title = "App Rules"

        findViewById<MaterialCardView>(R.id.menuChangePinCard).setOnClickListener {
            startActivity(Intent(this, ChangePinActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.menuWallpaperCard).setOnClickListener {
            startActivity(Intent(this, WallpaperPickerActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.menuHiddenAppsCard).setOnClickListener {
            startActivity(Intent(this, HiddenAppsActivity::class.java))
        }
    }
}
