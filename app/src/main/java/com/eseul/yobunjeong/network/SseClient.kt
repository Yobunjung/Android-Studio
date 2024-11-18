import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SseClient(private val url: String, private val listener: SseListener) {

    interface SseListener {
        fun onMessageReceived(data: JSONObject)
        fun onError(error: String)
    }

    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS) // SSE 연결 유지
        .build()

    private var eventSource: EventSource? = null

    fun connect() {
        val request = Request.Builder()
            .url(url)
            .build()

        // SSE 연결 생성
        eventSource = EventSources.createFactory(client).newEventSource(request, object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                println("SSE 연결 열림")
            }

            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                println("SSE 메시지 수신: $data")
                try {
                    val json = JSONObject(data)
                    listener.onMessageReceived(json)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onError("잘못된 데이터 형식")
                }
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?
            ) {
                println("SSE 연결 오류: ${t?.message}")
                listener.onError(t?.message ?: "알 수 없는 오류")
            }

            override fun onClosed(eventSource: EventSource) {
                println("SSE 연결 닫힘")
            }
        })
    }

    fun disconnect() {
        eventSource?.cancel()
        println("SSE 연결 종료")
    }
}
