package com.eseul.yobunjeong.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eseul.yobunjeong.fragment.DonationFragment
import com.eseul.yobunjeong.fragment.StoreFragment

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2  // 상점과 기부 두 개 탭

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StoreFragment()  // 상점 탭의 프래그먼트
            1 -> DonationFragment()  // 기부 탭의 프래그먼트
            else -> StoreFragment()
        }
    }
}
