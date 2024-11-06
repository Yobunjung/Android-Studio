package com.eseul.yobunjeong

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eseul.yobunjeong.adapter.UsageAdapter
import com.eseul.yobunjeong.viewmodel.UsageViewModel // 여기서 UsageViewModel을 임포트합니다.

class UsageActivity : AppCompatActivity() {
    private lateinit var usageViewModel: UsageViewModel // UsageViewModel로 변경
    private lateinit var usageAdapter: UsageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)

        // 뒤로 가기 버튼 설정
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        // RecyclerView 설정
        val pointsRecyclerView: RecyclerView = findViewById(R.id.points_recycler_view)
        pointsRecyclerView.layoutManager = LinearLayoutManager(this)

        // 어댑터 설정
        usageAdapter = UsageAdapter()
        pointsRecyclerView.adapter = usageAdapter

        // 점선 구분선 추가
        val dashedDivider = object : DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                val paint = Paint().apply {
                    color = getColor(R.color.dividerColor) // colors.xml에서 정의된 dividerColor
                    style = Paint.Style.STROKE
                    strokeWidth = 2f
                    pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
                }
                val left = parent.paddingLeft
                val right = parent.width - parent.paddingRight
                for (i in 0 until parent.childCount) {
                    val child = parent.getChildAt(i)
                    val params = child.layoutParams as RecyclerView.LayoutParams
                    val top = child.bottom + params.bottomMargin
                    canvas.drawLine(left.toFloat(), top.toFloat(), right.toFloat(), top.toFloat(), paint)
                }
            }
        }
        pointsRecyclerView.addItemDecoration(dashedDivider)

        // ViewModel 설정 및 데이터 관찰
        usageViewModel = ViewModelProvider(this).get(UsageViewModel::class.java) // 정확한 ViewModel 클래스 지정
        val userId = 1 // 실제 사용자 ID로 변경
        usageViewModel.getRecycleLogs(userId).observe(this, Observer { recycleLogs ->
            if (recycleLogs != null && recycleLogs.isNotEmpty()) {
                usageAdapter.submitList(recycleLogs)
            } else {
                Toast.makeText(this, "재활용 내역을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
