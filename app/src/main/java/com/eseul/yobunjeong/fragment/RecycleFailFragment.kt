package com.eseul.yobunjeong.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.eseul.yobunjeong.R

class RecycleFailFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycle_fail, container, false)
        val btnRetry: Button = view.findViewById(R.id.btnRetry)

        btnRetry.setOnClickListener {
            // 뒤로가기 처리
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }
}
