package com.eseul.yobunjeong.fragment

import RecycleViewModel
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.viewmodel.CameraViewModel

class QrCodeFragment : Fragment() {

    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var recycleViewModel: RecycleViewModel
    private lateinit var ivQrCode: ImageView
    private lateinit var tvExpiration: TextView
    private lateinit var btnConfirm: Button
    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_qr_code, container, false)
        ivQrCode = view.findViewById(R.id.ivQrCode)
        tvExpiration = view.findViewById(R.id.tvExpiration)
        btnConfirm = view.findViewById(R.id.btnConfirm)

        cameraViewModel = ViewModelProvider(requireActivity()).get(CameraViewModel::class.java)
        recycleViewModel = ViewModelProvider(requireActivity()).get(RecycleViewModel::class.java)

        observeRecycleStatus() // 상태 관찰 추가

        cameraViewModel.qrCodeBitmap.observe(viewLifecycleOwner) { bitmap ->
            if (bitmap != null) {
                ivQrCode.setImageBitmap(bitmap)
                startCountdownTimer()

                // QR 코드 성공 시 SSE 연결 시작
                recycleViewModel.startSse(userId = 1) // userId는 실제 값으로 설정
            } else {
                tvExpiration.text = "QR 코드 불러오기 실패"
            }
        }

        btnConfirm.setOnClickListener {
            countDownTimer?.cancel()
            recycleViewModel.stopSse() // SSE 연결 중단
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun observeRecycleStatus() {
        recycleViewModel.recycleStatus.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                if (status.success) {
                    // SSE 성공 시 화면 전환
                    navigateToRecycleCompleteFragment()
                }
            }
        }

        recycleViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToRecycleCompleteFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, RecycleCompleteFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun startCountdownTimer() {
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                tvExpiration.text = "유효 시간 - $secondsLeft 초"
            }

            override fun onFinish() {
                tvExpiration.text = "유효 시간이 만료되었습니다."
                ivQrCode.setImageBitmap(null)
                recycleViewModel.stopSse()
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        recycleViewModel.stopSse()
    }
}

