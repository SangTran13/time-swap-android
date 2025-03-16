package timeswap.application.data.request

import com.google.gson.annotations.SerializedName
import timeswap.application.data.entity.ChatMessage

data class ChatRequest(

    @SerializedName("model") val model: String = "gpt-4o-mini",
    @SerializedName("messages") val messages: List<ChatMessage>

)