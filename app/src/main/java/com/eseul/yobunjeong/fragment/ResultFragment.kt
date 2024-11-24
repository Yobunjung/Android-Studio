package com.eseul.yobunjeong.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eseul.yobunjeong.Factory.CameraViewModelFactory
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.repository.ImageRepository
import com.eseul.yobunjeong.viewmodel.CameraViewModel

class ResultFragment : Fragment() {

    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var tvRecognitionResult: TextView
    private lateinit var btnConfirm: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        tvRecognitionResult = view.findViewById(R.id.tvRecognitionResult)
        btnConfirm = view.findViewById(R.id.btnConfirm)

        // ViewModel 초기화
        val repository = ImageRepository()
        val factory = CameraViewModelFactory(repository)
        cameraViewModel = ViewModelProvider(requireActivity(), factory).get(CameraViewModel::class.java)

        // 결과 표시
        val resultText = arguments?.getString("recognition_result") ?: "결과 없음"
        tvRecognitionResult.text = resultText

        //확인버튼 클릭 이벤트
        btnConfirm.setOnClickListener {
            cameraViewModel.requestQrCode(trashType = resultText, userId = 1)
            showQrCodeFragment()
        }

        return view
    }

    private fun showQrCodeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, QrCodeFragment())
            .addToBackStack(null)
            .commit()
    }
}
