package com.eseul.yobunjeong

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eseul.yobunjeong.adapter.UsageAdapter
import com.eseul.yobunjeong.viewmodel.UsageViewModel

class UsageActivity : AppCompatActivity() {
    private lateinit var usageViewModel: UsageViewModel
    private lateinit var usageAdapter: UsageAdapter
    private lateinit var plasticCountTextView: TextView
    private lateinit var paperCountTextView: TextView
    private lateinit var canCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)

        // 뒤로 가기 버튼 설정
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        // TextView 초기화
        plasticCountTextView = findViewById(R.id.plastic_count_text_view)
        paperCountTextView = findViewById(R.id.paper_count_text_view)
        canCountTextView = findViewById(R.id.can_count_text_view)

        // RecyclerView 설정
        val pointsRecyclerView: RecyclerView = findViewById(R.id.points_recycler_view)
        pointsRecyclerView.layoutManager = LinearLayoutManager(this)

        // 어댑터 설정
        usageAdapter = UsageAdapter()
        pointsRecyclerView.adapter = usageAdapter

        // 점선 구분선 추가
        pointsRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        // ViewModel 설정 및 데이터 관찰
        usageViewModel = ViewModelProvider(this).get(UsageViewModel::class.java)
        val userId = 1 // 실제 사용자 ID로 변경

        // ViewModel의 recycleLogs LiveData를 observe합니다.
        usageViewModel.getRecycleLogs(userId).observe(this, Observer { usageList ->
            if (usageList != null && usageList.isNotEmpty()) {
                Log.d("UsageActivity", "recycleCounts: ${usageList[0].recycleCounts}")
                Log.d("UsageActivity", "recentRecycles: ${usageList[0].recentRecycles}")

                // RecyclerView에 recentRecycles 데이터 리스트 설정
                val recentRecycles = usageList.flatMap { it.recentRecycles }
                usageAdapter.submitList(recentRecycles) // recentRecycles 리스트 전달

                // recycleCounts 데이터를 UI에 표시 (첫 번째 항목 사용 예)
                val firstUsage = usageList[0]
                plasticCountTextView.text = firstUsage.recycleCounts.plastic
                paperCountTextView.text = firstUsage.recycleCounts.paper
                canCountTextView.text = firstUsage.recycleCounts.can
            } else {
                Log.e("UsageActivity", "recycle logs 데이터가 비어있습니다.")
                Toast.makeText(this, "재활용 내역을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
