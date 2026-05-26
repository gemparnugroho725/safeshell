package com.example.safelauncher

import android.os.Bundle
import android.widget.Button

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HiddenAppsActivity : BaseSecureActivity() {
    private lateinit var hiddenPackages: MutableSet<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hidden_apps)

        title = "Ubah Aplikasi Muncul"

        val recyclerView = findViewById<RecyclerView>(R.id.rulesRecyclerView)
        val saveButton = findViewById<Button>(R.id.saveRulesButton)

        val allApps = AppCatalog.loadLaunchableApps(this)
        hiddenPackages = AppRulesStore.getHiddenPackages(this).toMutableSet()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = AppRulesAdapter(allApps, hiddenPackages) { packageName, checked ->
            if (checked) {
                hiddenPackages.add(packageName)
            } else {
                hiddenPackages.remove(packageName)
            }
        }

        saveButton.setOnClickListener {
            AppRulesStore.saveHiddenPackages(this, hiddenPackages)
            finish()
        }
    }

    override fun onBackPressed() {
        AppRulesStore.saveHiddenPackages(this, hiddenPackages)
        super.onBackPressed()
    }
}
