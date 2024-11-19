import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eseul.yobunjeong.data.RecycleStatus
import org.json.JSONObject

class RecycleViewModel : ViewModel(), SseClient.SseListener {

    private val _recycleStatus = MutableLiveData<RecycleStatus>()
    val recycleStatus: LiveData<RecycleStatus> get() = _recycleStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private var sseClient: SseClient? = null
    private var isSseConnected = false // SSE 연결 여부 플래그
    private var isSseStopped = false   // SSE 종료 여부 플래그

    fun startSse(userId: Int) {
        if (!isSseConnected && !isSseStopped) { // 중복 연결 및 종료 방지
            val url = "http://3.38.54.56:5000/recycle/${userId}/is_successful"
            println("SSE 연결 시작: $url")
            sseClient = SseClient(url, this)
            sseClient?.connect()
            isSseConnected = true // 연결 상태 설정
        }
    }

    fun stopSse() {
        if (isSseConnected && !isSseStopped) { // 연결이 활성화된 경우에만 종료
            sseClient?.disconnect()
            isSseStopped = true
            isSseConnected = false
            println("SSE 연결 종료 완료")
        }
    }

    override fun onMessageReceived(data: JSONObject) {
        if (!isSseConnected || isSseStopped) return // 이미 종료된 경우 처리 중단

        try {
            val message = data.getString("message")
            val success = data.getBoolean("is_successful")
            val points = data.optInt("earned_points", 0)

            val status = RecycleStatus(success, message, points)
            _recycleStatus.postValue(status)
            println("SSE 데이터 업데이트: $status")

            // 성공 메시지 수신 시 SSE 종료
            if (success) {
                println("성공 메시지 수신, SSE 종료 처리")
                stopSse()
            }
        } catch (e: Exception) {
            onError("잘못된 데이터 형식: ${e.message}")
        }
    }

    override fun onError(error: String) {
        if (!isSseConnected || isSseStopped) return // 이미 종료된 경우 처리 중단

        println("SSE 연결 오류 발생: $error")
        _errorMessage.postValue(error)

        // 오류 발생 시 SSE 연결 종료
        stopSse()
    }

    override fun onCleared() {
        super.onCleared()
        stopSse() // ViewModel이 삭제될 때 SSE 연결 종료
    }
}



