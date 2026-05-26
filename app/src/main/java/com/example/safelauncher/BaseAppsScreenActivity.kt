package com.example.safelauncher

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

abstract class BaseAppsScreenActivity : BaseSecureActivity() {
    private lateinit var appsPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val root = findViewById<android.view.View>(R.id.launcherRoot)
        root.setBackgroundResource(AppRulesStore.getSelectedBackgroundRes(this))

        setupDateHeader()
        setupAppsPager()
    }

    override fun onResume() {
        super.onResume()
        val root = findViewById<android.view.View>(R.id.launcherRoot)
        root.setBackgroundResource(AppRulesStore.getSelectedBackgroundRes(this))
        bindAppsToPager()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    protected abstract fun getAppsForScreen(): List<AppInfo>

    protected open fun onAppSelected(app: AppInfo): Boolean {
        return false
    }

    protected fun launchInstalledApp(app: AppInfo) {
        try {
            val launchIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                setClassName(app.packageName, app.activityName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(launchIntent)
        } catch (error: Exception) {
            Toast.makeText(this, "Aplikasi tidak bisa dibuka", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupDateHeader() {
        val dateText = findViewById<TextView>(R.id.dateText)
        val dayText = findViewById<TextView>(R.id.dayText)
        val now = Date()

        dateText.text = SimpleDateFormat("MMMM d", Locale.getDefault()).format(now)
        dayText.text = SimpleDateFormat("EEEE, yyyy", Locale.getDefault()).format(now).uppercase(Locale.getDefault())
    }

    private fun setupAppsPager() {
        appsPager = findViewById(R.id.appsPager)
        appsPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        appsPager.offscreenPageLimit = 1
        appsPager.overScrollMode = ViewPager2.OVER_SCROLL_NEVER
    }

    private fun bindAppsToPager() {
        val apps = getAppsForScreen()
        val pages = if (apps.isEmpty()) listOf(emptyList()) else apps.chunked(APPS_PER_PAGE)

        appsPager.adapter = AppsPagerAdapter(pages) { app ->
            if (!onAppSelected(app)) {
                launchInstalledApp(app)
            }
        }
    }

    companion object {
        private const val APPS_PER_PAGE = 24
    }
}
