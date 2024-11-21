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
            recycleViewModel.resetRecycleStatus() // 상태 초기화
            requireActivity().supportFragmentManager.popBackStack(
                null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            requireActivity()
                .findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
                .selectedItemId = R.id.home
        }



        return view
    }

    private fun observeViewModel() {
        // 상태 변경 관찰
        recycleViewModel.recycleStatus.observe(viewLifecycleOwner) { status ->
            status?.let {
                tvMessage.text = it.message
                tvPoints.text = "${it.earnedPoints} P"
                btnConfirm.visibility = if (it.success) View.VISIBLE else View.GONE
                if (it.success) recycleViewModel.stopSse()
            } ?: run {
                tvMessage.text = "결과 없음"
                tvPoints.text = ""
                btnConfirm.visibility = View.GONE
            }
        }

        // 에러 메시지 관찰
        recycleViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycleViewModel.stopSse() // Fragment 종료 시 SSE 연결 중단
        recycleViewModel.resetRecycleStatus() // ViewModel 상태 초기화
    }
}