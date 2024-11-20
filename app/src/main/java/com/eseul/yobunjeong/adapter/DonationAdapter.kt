package com.eseul.yobunjeong.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eseul.yobunjeong.databinding.ItemDonationBinding

class DonationAdapter(
    private val items: List<DonationItem>,
    private val onClick: (DonationItem) -> Unit
) : RecyclerView.Adapter<DonationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemDonationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DonationItem) {
            binding.donationImage.setImageResource(item.imageRes) // 이미지 설정
            binding.donationTitle.text = item.title               // 제목 설정
            binding.donationDescription.text = item.description   // 설명 설정
            binding.donateButton.setOnClickListener { onClick(item) } // 클릭 이벤트
        }
    }
}

data class DonationItem(
    val imageRes: Int,  // 이미지 리소스 ID
    val title: String,  // 제목
    val description: String  // 설명
)
