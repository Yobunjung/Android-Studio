package com.eseul.yobunjeong.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ShopFragment : Fragment() {
    private lateinit var tabAdapter: TabAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_shop.xml 레이아웃과 연결
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        // ViewPager2와 TabLayout 초기화
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)

        // ViewPager에 어댑터 연결
        tabAdapter = TabAdapter(this)
        viewPager.adapter = tabAdapter

        // TabLayout과 ViewPager2를 동기화
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "상점"
                1 -> tab.text = "기부"
            }
        }.attach()

        return view
    }
}