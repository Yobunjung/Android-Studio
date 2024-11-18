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

    fun startSse(userId: Int) {
        val url = "http://3.38.54.56:5000/recycle/${userId}/is_successful"
        println("SSE 연결 시작: $url")
        sseClient = SseClient(url, this)
        sseClient?.connect()
    }

    fun stopSse() {
        sseClient?.disconnect()
    }

    override fun onMessageReceived(data: JSONObject) {
        println("SSE 데이터 수신: $data") // 디버그 로그 추가
        val message = data.getString("message")
        val success = data.getBoolean("is_successful")
        val points = data.optInt("earned_points", 0)

        val status = RecycleStatus(success, message, points)
        _recycleStatus.postValue(status) // LiveData 업데이트
        println("RecycleStatus 업데이트됨: $status")
    }

    override fun onError(error: String) {
        _errorMessage.postValue(error)
    }
}
