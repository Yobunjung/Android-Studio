
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eseul.yobunjeong.data.RecycleStatus
import org.json.JSONObject

class RecycleViewModel : ViewModel(), SseClient.SseListener {

    private val _recycleStatus = MutableLiveData<RecycleStatus?>()
    val recycleStatus: MutableLiveData<RecycleStatus?> get() = _recycleStatus

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> get() = _errorMessage

    private var sseClient: SseClient? = null
    private var isSseConnected = false
    private var isSseStopped = false

    fun startSse(userId: Int) {
        if (isSseConnected || isSseStopped) {
            println("SSE 연결이 이미 설정되어 있거나 종료 상태입니다.")
            return
        }
        val url = "http://3.38.54.56:5000/recycle/${userId}/is_successful"
        println("SSE 연결 시작: $url")
        sseClient = SseClient(url, this)
        sseClient?.connect()
        isSseConnected = true
        isSseStopped = false
    }

    fun stopSse() {
        if (!isSseConnected || isSseStopped) {
            println("SSE 연결이 이미 종료되었습니다.")
            return
        }
        sseClient?.disconnect()
        isSseStopped = true
        isSseConnected = false
        println("SSE 연결 종료 완료")
    }

    fun resetRecycleStatus() {
        _recycleStatus.postValue(null)
        _errorMessage.postValue(null)
        isSseStopped = false
        isSseConnected = false
    }

    override fun onMessageReceived(data: JSONObject) {
        if (!isSseConnected || isSseStopped) return

        try {
            val message = data.getString("message")
            val success = data.getBoolean("is_successful")
            val points = data.optInt("earned_points", 0)

            val status = RecycleStatus(success, message, points)
            _recycleStatus.postValue(status)
            println("SSE 데이터 업데이트: $status")

            if (success) {
                stopSse()
            }
        } catch (e: Exception) {
            onError("잘못된 데이터 형식: ${e.message}")
        }
    }

    override fun onError(error: String) {
        if (!isSseConnected || isSseStopped) return

        println("SSE 연결 오류 발생: $error")
        _errorMessage.postValue(error)
        stopSse()
    }

    override fun onCleared() {
        super.onCleared()
        stopSse()
    }
}




