package com.eseul.yobunjeong.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.model.PointLog

class PointLogAdapter(private val pointLogs: List<PointLog>) :
    RecyclerView.Adapter<PointLogAdapter.PointLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointLogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_point_log, parent, false)
        return PointLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: PointLogViewHolder, position: Int) {
        val log = pointLogs[position]
        holder.bind(log)
    }

    override fun getItemCount(): Int = pointLogs.size

    inner class PointLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.date_text)
        private val pointsChangeTextView: TextView = itemView.findViewById(R.id.points_change_text)

        fun bind(log: PointLog) {
            dateTextView.text = log.timestamp
            pointsChangeTextView.text = "+${log.pointsChange} P"
        }
    }
}
