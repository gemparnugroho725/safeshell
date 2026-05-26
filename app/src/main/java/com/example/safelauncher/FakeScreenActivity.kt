package com.example.safelauncher

class FakeScreenActivity : BaseAppsScreenActivity() {
    override fun onResume() {
        super.onResume()
        AppRulesStore.saveLastScreen(this, AppRulesStore.SCREEN_FAKE)
    }

    override fun getAppsForScreen(): List<AppInfo> {
        return AppCatalog.getFakeScreenApps(this)
    }
}
