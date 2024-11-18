package com.eseul.yobunjeong.fragment

import RecycleViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eseul.yobunjeong.R


class RecycleCompleteFragment : Fragment() {

    private lateinit var recycleViewModel: RecycleViewModel
    private lateinit var tvMessage: TextView
    private lateinit var tvPoints: TextView
    private lateinit var btnConfirm: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycle_complete, container, false)
        tvMessage = view.findViewById(R.id.tvMessage)
        tvPoints = view.findViewById(R.id.tvPoints)
        btnConfirm = view.findViewById(R.id.btnConfirm)

        recycleViewModel = ViewModelProvider(requireActivity()).get(RecycleViewModel::class.java)

        observeViewModel()

        // Start SSE connection
        recycleViewModel.startSse(userId = 1)

        btnConfirm.setOnClickListener {
            recycleViewModel.stopSse() // Stop SSE connection
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun observeViewModel() {
        recycleViewModel.recycleStatus.observe(viewLifecycleOwner) { status ->
            if (status.success) {
                tvMessage.text = "분리수거 완료! ${status.message}"
                tvPoints.text = "+ ${status.earnedPoints} P"
                btnConfirm.visibility = View.VISIBLE
                recycleViewModel.stopSse() // 성공 시 SSE 연결 종료
            } else {
                tvMessage.text = status.message
                tvPoints.text = ""
            }
        }

        recycleViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        // 기존 데이터 즉시 반영
        recycleViewModel.recycleStatus.value?.let { status ->
            if (status.success) {
                tvMessage.text = "분리수거 완료! ${status.message}"
                tvPoints.text = "+ ${status.earnedPoints} P"
                btnConfirm.visibility = View.VISIBLE
            } else {
                tvMessage.text = status.message
                tvPoints.text = ""
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycleViewModel.stopSse() // Fragment 종료 시 SSE 연결 중단
    }
}
