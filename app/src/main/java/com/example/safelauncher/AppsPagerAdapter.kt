package com.example.safelauncher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AppsPagerAdapter(
    private val pages: List<List<AppInfo>>,
    private val onAppClick: (AppInfo) -> Unit
) : RecyclerView.Adapter<AppsPagerAdapter.PageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_apps_page, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position], onAppClick)
    }

    override fun getItemCount(): Int = pages.size

    class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pageRecyclerView: RecyclerView = itemView.findViewById(R.id.pageAppsRecyclerView)

        init {
            pageRecyclerView.layoutManager = GridLayoutManager(itemView.context, 4)
            pageRecyclerView.setHasFixedSize(true)
            pageRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            pageRecyclerView.isNestedScrollingEnabled = false
        }

        fun bind(pageApps: List<AppInfo>, onAppClick: (AppInfo) -> Unit) {
            pageRecyclerView.adapter = AppListAdapter(pageApps, onAppClick)
        }
    }
}
