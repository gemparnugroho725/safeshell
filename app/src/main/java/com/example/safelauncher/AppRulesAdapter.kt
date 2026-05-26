package com.example.safelauncher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView

class AppRulesAdapter(
    private val items: List<AppInfo>,
    hiddenPackages: Set<String>,
    private val onCheckedChange: (packageName: String, checked: Boolean) -> Unit
) : RecyclerView.Adapter<AppRulesAdapter.AppRuleViewHolder>() {

    private val selectedPackages = hiddenPackages.toMutableSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppRuleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rule_app, parent, false)
        return AppRuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppRuleViewHolder, position: Int) {
        holder.bind(items[position], selectedPackages.contains(items[position].packageName), onCheckedChange) { packageName, isChecked ->
            if (isChecked) selectedPackages.add(packageName) else selectedPackages.remove(packageName)
        }
    }

    override fun getItemCount(): Int = items.size

    class AppRuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appIcon: ImageView = itemView.findViewById(R.id.ruleAppIcon)
        private val appLabel: TextView = itemView.findViewById(R.id.ruleAppLabel)
        private val appPackage: TextView = itemView.findViewById(R.id.ruleAppPackage)
        private val appCheck: AppCompatCheckBox = itemView.findViewById(R.id.ruleAppCheck)

        fun bind(
            item: AppInfo,
            checked: Boolean,
            onCheckedChange: (packageName: String, checked: Boolean) -> Unit,
            onLocalStateChanged: (packageName: String, checked: Boolean) -> Unit
        ) {
            appIcon.setImageDrawable(item.icon)
            appLabel.text = item.label
            appPackage.text = item.packageName

            appCheck.setOnCheckedChangeListener(null)
            appCheck.isChecked = checked

            itemView.setOnClickListener {
                appCheck.isChecked = !appCheck.isChecked
            }

            appCheck.setOnCheckedChangeListener { _, isChecked ->
                onLocalStateChanged(item.packageName, isChecked)
                onCheckedChange(item.packageName, isChecked)
            }
        }
    }
}
