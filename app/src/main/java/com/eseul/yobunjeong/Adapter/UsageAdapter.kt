package com.eseul.yobunjeong.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.model.RecentRecycle

class UsageAdapter : ListAdapter<RecentRecycle, UsageAdapter.UsageViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_log, parent, false)
        return UsageViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UsageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binTypeTextView: TextView = itemView.findViewById(R.id.bin_type_text)
        // private val recycleCountTextView: TextView = itemView.findViewById(R.id.recycle_count_text)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestamp_text)

        fun bind(log: RecentRecycle) {
            binTypeTextView.text = log.trashType
            //recycleCountTextView.text = "점수: ${log.earnedPoints}"
            timestampTextView.text = log.timestamp
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecentRecycle>() {
            override fun areItemsTheSame(oldItem: RecentRecycle, newItem: RecentRecycle): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: RecentRecycle, newItem: RecentRecycle): Boolean {
                return oldItem == newItem
            }
        }
    }
}
