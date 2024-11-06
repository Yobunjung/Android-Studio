package com.eseul.yobunjeong.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.model.QuestionRequest
import com.eseul.yobunjeong.model.RecycleResponse
import com.eseul.yobunjeong.network.RecycleGuideApi
import com.eseul.yobunjeong.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuideFragment : Fragment() {

    private lateinit var searchButton: ImageButton
    private lateinit var queryEditText: EditText
    private lateinit var resultTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃을 인플레이트하고 뷰를 참조합니다.
        val view = inflater.inflate(R.layout.fragment_guide, container, false)

        // 뷰 요소들 초기화
        searchButton = view.findViewById(R.id.search_button)
        queryEditText = view.findViewById(R.id.search_edit_text)
        resultTextView = view.findViewById(R.id.result_text_view)

        // 검색 버튼 클릭 리스너
        searchButton.setOnClickListener {
            val question = queryEditText.text.toString()
            if (question.isNotEmpty()) {
                getRecycleInfo(question)  // API 요청 호출
            }
        }

        return view
    }

    // 서버에 API 요청을 보내고 결과를 받아오는 함수
    private fun getRecycleInfo(question: String) {
        val api = RetrofitClient.instance.create(RecycleGuideApi::class.java)
        val call = api.getRecycleInfo(QuestionRequest(question))

        call.enqueue(object : Callback<RecycleResponse> {
            override fun onResponse(call: Call<RecycleResponse>, response: Response<RecycleResponse>) {
                if (response.isSuccessful) {
                    val recycleResponse = response.body()
                    if (recycleResponse != null) {
                        // 서버로부터 받은 응답을 결과 텍스트 뷰에 표시
                        resultTextView.text = recycleResponse.answer
                    } else {
                        resultTextView.text = "정보를 찾을 수 없습니다."
                    }
                } else {
                    resultTextView.text = "API 요청 실패"
                }
            }

            override fun onFailure(call: Call<RecycleResponse>, t: Throwable) {
                resultTextView.text = "네트워크 오류: ${t.message}"
            }
        })
    }
}
