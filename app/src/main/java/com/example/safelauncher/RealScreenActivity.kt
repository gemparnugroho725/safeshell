package com.example.safelauncher

import android.content.Intent

class RealScreenActivity : BaseAppsScreenActivity() {
    override fun onResume() {
        super.onResume()
        AppRulesStore.saveLastScreen(this, AppRulesStore.SCREEN_REAL)
    }

    override fun getAppsForScreen(): List<AppInfo> {
        return AppCatalog.getRealScreenApps(this)
    }

    override fun onAppSelected(app: AppInfo): Boolean {
        if (app.packageName != AppRulesStore.RULES_MENU_PACKAGE) {
            return false
        }

        startActivity(Intent(this, AppRulesActivity::class.java))
        return true
    }
}
