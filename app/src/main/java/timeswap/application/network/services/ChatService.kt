package timeswap.application.network.services

import retrofit2.http.Body
import retrofit2.http.POST
import timeswap.application.data.entity.ChatMessage
import timeswap.application.data.request.ChatRequest
import timeswap.application.data.response.ChatResponse
import timeswap.application.network.RetrofitClient

interface ChatService {
    @POST("chat/completions")
    suspend fun getChatResponse(@Body request: ChatRequest): ChatResponse
}

class ChatRepository {
    suspend fun sendMessage(messages: List<ChatMessage>): String? {
        return try {
            val response = RetrofitClient.chatService.getChatResponse(ChatRequest(messages = messages))
            response.choices.firstOrNull()?.message?.content
        } catch (e: Exception) {
            e.printStackTrace()
            "Lỗi API: ${e.localizedMessage}"
        }
    }
}
