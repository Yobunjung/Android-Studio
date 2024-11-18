package com.eseul.yobunjeong

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eseul.yobunjeong.adapter.PointLogAdapter
import com.eseul.yobunjeong.model.PointModel
import com.eseul.yobunjeong.viewmodel.PointViewModel

class PointActivity : AppCompatActivity() {
    private lateinit var pointViewModel: PointViewModel
    private lateinit var pointsTextView: TextView
    private lateinit var pointsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)

        // UI 요소 초기화
        val backButton: ImageButton = findViewById(R.id.back_button)
        pointsTextView = findViewById(R.id.points_text)
        pointsRecyclerView = findViewById(R.id.points_recycler_view)

        backButton.setOnClickListener {
            finish()
        }

        // 사용자 ID 설정 (예: 1)
        val userId = 1

        pointViewModel = ViewModelProvider(this).get(PointViewModel::class.java)

        // ViewModel에서 데이터를 가져와 관찰합니다.
        pointViewModel.getPointsLogs(userId).observe(this, Observer { pointData ->
            if (pointData != null) {
                updateUI(pointData)
            } else {
                Toast.makeText(this, "포인트 데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(pointData: PointModel) {
        // 총 포인트 텍스트 업데이트
        pointsTextView.text = "${pointData.totalPoints} P"

        // RecyclerView 설정
        pointsRecyclerView.layoutManager = LinearLayoutManager(this)
        pointsRecyclerView.adapter = PointLogAdapter(pointData.pointsLogs)

        // 점선 구분선을 위한 DividerItemDecoration 추가
        val divider = object : DividerItemDecoration(this, VERTICAL) {
            override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                val paint = Paint().apply {
                    color = getColor(R.color.dividerColor)
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
                    val bottom = top + 2 // 구분선 두께

                    canvas.drawLine(left.toFloat(), top.toFloat(), right.toFloat(), top.toFloat(), paint)
                }
            }
        }
        pointsRecyclerView.addItemDecoration(divider)
    }
}
