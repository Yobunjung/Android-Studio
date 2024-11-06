package com.eseul.yobunjeong.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.model.UsageModel

class UsageAdapter : ListAdapter<UsageModel, UsageAdapter.UsageViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_log, parent, false)
        return UsageViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UsageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binTypeTextView: TextView = itemView.findViewById(R.id.bin_type_text)
        private val recycleCountTextView: TextView = itemView.findViewById(R.id.recycle_count_text)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestamp_text)

        fun bind(log: UsageModel) {
            binTypeTextView.text = log.binType
            recycleCountTextView.text = "횟수: ${log.recycleCount}"
            timestampTextView.text = log.timestamp
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UsageModel>() {
            override fun areItemsTheSame(oldItem: UsageModel, newItem: UsageModel): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: UsageModel, newItem: UsageModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
