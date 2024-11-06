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
import com.eseul.yobunjeong.PointActivity
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.UsageActivity
import com.eseul.yobunjeong.model.HomeModel
import com.eseul.yobunjeong.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    // ViewModel 초기화
    private lateinit var  homeViewModel: HomeViewModel

    private lateinit var pointsTextView: TextView
    private lateinit var usageImageView:ImageView
    private lateinit var nicknameTextView: TextView
    private lateinit var plasticCountTextView: TextView
    private lateinit var paperCountTextView: TextView
    private lateinit var canCountTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_home.xml을 inflate
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // UI 요소 초기화
        pointsTextView = view.findViewById(R.id.points_text_view)
        usageImageView=view.findViewById(R.id.usage_list)
        nicknameTextView = view.findViewById(R.id.nickname_text_view)
        plasticCountTextView = view.findViewById(R.id.plastic_count_text_view)
        paperCountTextView = view.findViewById(R.id.paper_count_text_view)
        canCountTextView = view.findViewById(R.id.can_count_text_view)

        // 포인트 TextView 클릭 시 PointActivity로 이동
        pointsTextView.setOnClickListener {
            val intent = Intent(requireContext(), PointActivity::class.java)
            startActivity(intent)
        }

        // 이용 내역 ImageView 클릭 시 UsageActivity로 이동
        usageImageView.setOnClickListener {
            val intent=Intent(requireContext(),UsageActivity::class.java)
            startActivity(intent)
        }

        // ViewModelProvider를 사용하여 ViewModel 초기화
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // ViewModel에서 데이터를 관찰하여 UI 업데이트
        homeViewModel.homeData.observe(viewLifecycleOwner, Observer { homeData ->
            if (homeData != null) {
                updateUI(homeData)
            } else {
                Toast.makeText(requireContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
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
