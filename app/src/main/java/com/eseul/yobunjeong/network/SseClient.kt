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
    private var isClosedManually = false // 수동 종료 여부 확인 플래그

    fun connect() {
        if (eventSource != null) {
            println("SSE 연결이 이미 열려 있습니다.")
            return
        }
        val request = Request.Builder()
            .url(url)
            .build()

        eventSource = EventSources.createFactory(client).newEventSource(request, object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                println("SSE 연결 열림")
                isClosedManually = false
            }

            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                if (!isClosedManually) {
                    try {
                        val json = JSONObject(data)
                        listener.onMessageReceived(json)
                    } catch (e: Exception) {
                        listener.onError("잘못된 데이터 형식: ${e.message}")
                    }
                }
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?
            ) {
                if (!isClosedManually) {
                    val errorMessage = response?.let {
                        "서버 오류: ${it.code} - ${it.message}"
                    } ?: t?.message ?: "알 수 없는 오류"
                    listener.onError(errorMessage)
                    println("SSE 연결 실패: $errorMessage")
                    disconnect()
                }
            }

            override fun onClosed(eventSource: EventSource) {
                if (!isClosedManually) {
                    println("SSE 연결 닫힘 (서버)")
                }
                disconnect()
            }
        })
    }

    fun disconnect() {
        if (eventSource != null) {
            println("SSE 연결 종료")
            isClosedManually = true
            eventSource?.cancel()
            eventSource = null
        }
    }
}
