package com.eseul.yobunjeong.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.viewmodel.RecycleViewModel

class RecycleCompleteFragment : Fragment() {

    private lateinit var recycleViewModel: RecycleViewModel
    private lateinit var tvMessage: TextView
    private lateinit var tvPoints: TextView
    private lateinit var btnConfirm: Button
    private val handler = Handler(Looper.getMainLooper())
    private var isPolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycle_complete, container, false)
        tvMessage = view.findViewById(R.id.tvMessage)
        tvPoints = view.findViewById(R.id.tvPoints)
        btnConfirm = view.findViewById(R.id.btnConfirm)

        recycleViewModel = ViewModelProvider(this).get(RecycleViewModel::class.java)

        observeViewModel()

        // QR 코드 생성 요청
        recycleViewModel.generateQrCode()

        btnConfirm.setOnClickListener {
            isPolling = false // 폴링 중지
            handler.removeCallbacksAndMessages(null) // 핸들러 콜백 제거
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view
    }

    private fun observeViewModel() {
        // QR 코드 생성 상태 관찰
        recycleViewModel.qrCodeGenerated.observe(viewLifecycleOwner, Observer { isGenerated ->
            if (isGenerated) {
                Log.d("Fragment", "QR 코드 생성 완료. 폴링 시작")
                isPolling = true
                startPolling() // QR 코드 생성 후 폴링 시작
            }
        })

        // Recycle 상태 관찰
        recycleViewModel.recycleStatus.observe(viewLifecycleOwner, Observer { status ->
            Log.d("Fragment", "Recycle 상태 업데이트: $status")
            when {
                status.success -> {
                    showResultScreen("분리수거 완료! ${status.message}", "+ ${status.earnedPoints} P")
                    isPolling = false
                }
                !status.success && status.earnedPoints == 0 -> {
                    showResultScreen(status.message, "")
                    isPolling = false
                }
                else -> {
                    tvMessage.text = status.message
                    tvPoints.text = ""
                }
            }
        })

        // 오류 메시지 관찰
        recycleViewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            Log.e("Fragment", "에러 발생: $error")
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        })
    }

    private fun startPolling() {
        Log.d("Polling", "폴링 작업 시작")
        val pollingRunnable = object : Runnable {
            override fun run() {
                if (!isPolling) {
                    Log.d("Polling", "폴링 중지됨")
                    return
                }
                Log.d("Polling", "fetchRecycleStatus 호출")
                recycleViewModel.fetchRecycleStatus()
                handler.postDelayed(this, 2000) // 2초마다 반복
            }
        }

        handler.post(pollingRunnable) // 폴링 작업 시작
    }

    private fun showResultScreen(message: String, points: String) {
        tvMessage.text = message
        tvPoints.text = points
        btnConfirm.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isPolling = false // Fragment 종료 시 폴링 중지
        handler.removeCallbacksAndMessages(null)
    }
}
