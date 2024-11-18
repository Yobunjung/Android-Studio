package com.eseul.yobunjeong.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eseul.yobunjeong.PointActivity
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.UsageActivity
import com.eseul.yobunjeong.adapter.UsageAdapter
import com.eseul.yobunjeong.model.HomeModel
import com.eseul.yobunjeong.viewmodel.HomeViewModel
import com.eseul.yobunjeong.viewmodel.UsageViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var usageViewModel: UsageViewModel
    private lateinit var usageAdapterFixed: UsageAdapter

    private lateinit var pointsTextView: TextView
    private lateinit var usageImageView: ImageView
    private lateinit var nicknameTextView: TextView
    private lateinit var plasticCountTextView: TextView
    private lateinit var paperCountTextView: TextView
    private lateinit var canCountTextView: TextView
    private lateinit var recycleLogsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI 요소 초기화
        pointsTextView = view.findViewById(R.id.points_text_view)
        usageImageView = view.findViewById(R.id.usage_list)
        nicknameTextView = view.findViewById(R.id.nickname_text_view)
        plasticCountTextView = view.findViewById(R.id.plastic_count_text_view)
        paperCountTextView = view.findViewById(R.id.paper_count_text_view)
        canCountTextView = view.findViewById(R.id.can_count_text_view)
        recycleLogsRecyclerView = view.findViewById(R.id.recycle_logs_recycler_view)

        // 포인트 TextView 클릭 시 PointActivity로 이동
        pointsTextView.setOnClickListener {
            val intent = Intent(requireContext(), PointActivity::class.java)
            startActivity(intent)
        }

        // 이용 내역 ImageView 클릭 시 UsageActivity로 이동
        usageImageView.setOnClickListener {
            val intent = Intent(requireContext(), UsageActivity::class.java)
            startActivity(intent)
        }

        // RecyclerView 초기화
        recycleLogsRecyclerView.layoutManager = LinearLayoutManager(context)
        usageAdapterFixed = UsageAdapter()
        recycleLogsRecyclerView.adapter = usageAdapterFixed
        recycleLogsRecyclerView.isNestedScrollingEnabled = false // 스크롤 비활성화

        // ViewModel 초기화
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        usageViewModel = ViewModelProvider(this).get(UsageViewModel::class.java)

        // HomeViewModel에서 homeData를 관찰하여 UI 업데이트
        homeViewModel.homeData.observe(viewLifecycleOwner, Observer { homeData ->
            if (homeData != null) {
                updateUI(homeData)
            } else {
                Toast.makeText(requireContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        // UsageViewModel에서 재활용 내역 리스트 관찰
        observeRecycleLogs()
    }

    private fun observeRecycleLogs() {
        val userId = 1 // 실제 사용자 ID로 변경
        usageViewModel.getRecycleLogs(userId).observe(viewLifecycleOwner, Observer { usageList ->
            if (usageList != null && usageList.isNotEmpty()) {
                // 상위 5개의 데이터만 설정
                val fixedLogs = usageList.flatMap { it.recentRecycles }.take(5)
                usageAdapterFixed.submitList(fixedLogs)
            } else {
                Toast.makeText(context, "재활용 내역을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(homeData: HomeModel) {
        // 받아온 데이터로 UI 업데이트
        nicknameTextView.text = "${homeData.nickname}님!"
        pointsTextView.text = "${homeData.points}P"
        plasticCountTextView.text = homeData.recycleCounts.plastic
        paperCountTextView.text = homeData.recycleCounts.paper
        canCountTextView.text = homeData.recycleCounts.can
    }
}
