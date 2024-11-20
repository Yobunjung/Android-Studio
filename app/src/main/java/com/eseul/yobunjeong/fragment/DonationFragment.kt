package com.eseul.yobunjeong.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.adapter.DonationAdapter
import com.eseul.yobunjeong.adapter.DonationItem
import com.eseul.yobunjeong.viewmodel.HomeViewModel

class DonationFragment : Fragment() {

    private lateinit var donationRecyclerView: RecyclerView
    private lateinit var donationAdapter: DonationAdapter
    private lateinit var pointsText: TextView
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation, container, false)

        // 포인트 텍스트뷰 초기화
        pointsText = view.findViewById(R.id.points_text)

        // ViewModel 초기화
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        // ViewModel의 데이터를 관찰
        homeViewModel.homeData.observe(viewLifecycleOwner, Observer { homeData ->
            if (homeData != null) {
                pointsText.text = "${homeData.points}P"
            }
        })


        // RecyclerView 설정
        donationRecyclerView = view.findViewById(R.id.donation_recycler_view)
        donationRecyclerView.layoutManager = LinearLayoutManager(context)
        donationAdapter = DonationAdapter(getDonationItems()) { donationItem ->
            handleDonationClick(donationItem)
        }
        donationRecyclerView.adapter = donationAdapter

        return view
    }

    private fun handleDonationClick(donationItem: DonationItem) {
        // 클릭 이벤트 처리
        android.widget.Toast.makeText(
            context,
            "${donationItem.title} 기부완료!",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }

    private fun getDonationItems(): List<DonationItem> {
        // 기부 항목 리스트 생성
        return listOf(
            DonationItem(R.drawable.donation1, "지찬핑 돕기", "팀장님 지찬핑을 도와주세요!"),
            DonationItem(R.drawable.donation2, "혁핑 돕기", "백엔드 혁핑을 도와주세요!"),
            DonationItem(R.drawable.donation3, "지석핑 돕기", "아두이노 지석핑을 도와주세요!"),
            DonationItem(R.drawable.donation4, "이슬핑 돕기", "프론트엔드 이슬핑을 도와주세요!"),
            DonationItem(R.drawable.donation5, "인영핑 돕기", "프론트엔드 인영핑을 도와주세요!")
        )
    }
}
