package com.eseul.yobunjeong.fragment

import StoreAdapter
import StoreItem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.viewmodel.HomeViewModel

class StoreFragment : Fragment() {

    private lateinit var storeRecyclerView: RecyclerView
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var availablePointsText: TextView
    private lateinit var homeViewModel: HomeViewModel

    private val storeItems = listOf(
        StoreItem(R.drawable.store1, "엑소시트 찬", "1000P"),
        StoreItem(R.drawable.store2, "따봉 찬", "200P"),
        StoreItem(R.drawable.store3, "슬라임 찬", "300P"),
        StoreItem(R.drawable.store4, "오드아이 찬", "400P"),
        StoreItem(R.drawable.store5, "인...직 찬", "500P"),
        StoreItem(R.drawable.store6, "타 찬", "600P"),
        StoreItem(R.drawable.store7, "욕망의 항아리 찬 7", "700P"),
        StoreItem(R.drawable.store8, "건치 찬", "800P"),
        StoreItem(R.drawable.store9, "도둑 찬", "900P")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_store, container, false)

        // UI 요소 초기화
        availablePointsText = view.findViewById(R.id.available_points_text)
        storeRecyclerView = view.findViewById(R.id.store_recycler_view)
        storeRecyclerView.layoutManager = GridLayoutManager(context, 2) // 2열 그리드 레이아웃

        // ViewModel 초기화
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        // 포인트 데이터 관찰 (뷰가 초기화된 이후에 실행)
        homeViewModel.homeData.observe(viewLifecycleOwner, Observer { homeData ->
            if (homeData != null) {
                availablePointsText.text = "보유포인트: ${homeData.points}P"
            }
        })

        // RecyclerView 어댑터 설정
        storeAdapter = StoreAdapter(storeItems) { storeItem ->
            handleItemClick(storeItem)
        }
        storeRecyclerView.adapter = storeAdapter

        return view
    }

    private fun handleItemClick(storeItem: StoreItem) {
        // 아이템 클릭 시 처리 로직
        android.widget.Toast.makeText(
            context,
            "${storeItem.title} 구매하기 완료",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}
