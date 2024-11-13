package com.eseul.yobunjeong.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.viewmodel.CameraViewModel

class QrCodeFragment : Fragment() {

    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var ivQrCode: ImageView
    private lateinit var tvExpiration: TextView
    private lateinit var btnConfirm: Button
    private var countDownTimer: CountDownTimer? = null  // 타이머를 관리할 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_qr_code, container, false)
        ivQrCode = view.findViewById(R.id.ivQrCode)
        tvExpiration = view.findViewById(R.id.tvExpiration)
        btnConfirm = view.findViewById(R.id.btnConfirm)

        cameraViewModel = ViewModelProvider(requireActivity()).get(CameraViewModel::class.java)

        // QR 코드 LiveData 관찰
        cameraViewModel.qrCodeBitmap.observe(viewLifecycleOwner) { bitmap ->
            if (bitmap != null) {
                ivQrCode.setImageBitmap(bitmap)
                startCountdownTimer() // QR 코드가 표시될 때 타이머 시작
            } else {
                tvExpiration.text = "QR 코드 불러오기 실패"
            }
        }

        btnConfirm.setOnClickListener {
            countDownTimer?.cancel()  // 타이머 취소
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun startCountdownTimer() {
        // 30초 동안 1초마다 카운트다운
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 남은 시간(초) 계산
                val secondsLeft = millisUntilFinished / 1000
                tvExpiration.text = "유효 시간 - $secondsLeft 초"
            }

            override fun onFinish() {
                // 타이머가 종료되면 유효 시간 만료 메시지 표시
                tvExpiration.text = "유효 시간이 만료되었습니다."
                ivQrCode.setImageBitmap(null)  // QR 코드 제거
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fragment가 종료될 때 타이머 취소
        countDownTimer?.cancel()
    }
}
