package timeswap.application.data.entity

import com.google.gson.annotations.SerializedName

data class ChatMessage(

    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String

)

data class ChatChoice(

    @SerializedName("message") val message: ChatMessage

)