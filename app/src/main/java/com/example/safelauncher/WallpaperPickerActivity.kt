package com.example.safelauncher

import android.graphics.Color
import android.os.Bundle

import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

class WallpaperPickerActivity : BaseSecureActivity() {
    private lateinit var bgClassicCard: MaterialCardView
    private lateinit var bgOceanCard: MaterialCardView
    private lateinit var bgLightCard: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper_picker)

        title = "Pilih Wallpaper"

        bgClassicCard = findViewById(R.id.bgClassicCard)
        bgOceanCard = findViewById(R.id.bgOceanCard)
        bgLightCard = findViewById(R.id.bgLightCard)

        bgClassicCard.setOnClickListener { applyWallpaperSelection(AppRulesStore.BG_CLASSIC) }
        bgOceanCard.setOnClickListener { applyWallpaperSelection(AppRulesStore.BG_OCEAN) }
        bgLightCard.setOnClickListener { applyWallpaperSelection(AppRulesStore.BG_LIGHT) }

        updateWallpaperSelectionUi(AppRulesStore.getSelectedBackground(this))
    }

    private fun applyWallpaperSelection(selected: String) {
        AppRulesStore.saveSelectedBackground(this, selected)
        updateWallpaperSelectionUi(selected)
    }

    private fun updateWallpaperSelectionUi(selected: String) {
        val selectedColor = ContextCompat.getColor(this, android.R.color.holo_blue_light)
        val normalColor = Color.parseColor("#D3DCEB")

        bgClassicCard.strokeColor = if (selected == AppRulesStore.BG_CLASSIC) selectedColor else normalColor
        bgClassicCard.strokeWidth = if (selected == AppRulesStore.BG_CLASSIC) 3 else 1

        bgOceanCard.strokeColor = if (selected == AppRulesStore.BG_OCEAN) selectedColor else normalColor
        bgOceanCard.strokeWidth = if (selected == AppRulesStore.BG_OCEAN) 3 else 1

        bgLightCard.strokeColor = if (selected == AppRulesStore.BG_LIGHT) selectedColor else normalColor
        bgLightCard.strokeWidth = if (selected == AppRulesStore.BG_LIGHT) 3 else 1
    }
}
