package com.example.safelauncher

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

object AppCatalog {

    fun loadLaunchableApps(context: Context): List<AppInfo> {
        val pm = context.packageManager
        val currentPackage = context.packageName
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        return pm.queryIntentActivities(intent, 0)
            .asSequence()
            .filter { it.activityInfo.packageName != currentPackage }
            .map {
                AppInfo(
                    label = it.loadLabel(pm).toString(),
                    packageName = it.activityInfo.packageName,
                    activityName = it.activityInfo.name,
                    icon = it.loadIcon(pm)
                )
            }
            .sortedBy { it.label.lowercase() }
            .toList()
    }

    fun getRealScreenApps(context: Context): List<AppInfo> {
        val installedApps = loadLaunchableApps(context)
        val rulesApp = createRulesMenuApp(context)

        // Keep App Rules visible on the first page (4x6 grid = 24 slots),
        // with installed apps continuing on the next pages.
        if (installedApps.size <= 23) {
            return installedApps + rulesApp
        }

        val firstPageApps = installedApps.take(23)
        val remainingApps = installedApps.drop(23)
        return firstPageApps + rulesApp + remainingApps
    }

    fun getFakeScreenApps(context: Context): List<AppInfo> {
        val hiddenPackages = AppRulesStore.getHiddenPackages(context)
        return loadLaunchableApps(context)
            .filterNot { hiddenPackages.contains(it.packageName) }
    }

    private fun createRulesMenuApp(context: Context): AppInfo {
        val icon: Drawable = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_manage)
            ?: ColorDrawable(Color.TRANSPARENT)

        return AppInfo(
            label = "App Rules",
            packageName = AppRulesStore.RULES_MENU_PACKAGE,
            activityName = "",
            icon = icon
        )
    }
}
