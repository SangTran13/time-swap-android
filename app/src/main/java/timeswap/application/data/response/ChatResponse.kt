package timeswap.application.data.response

import com.google.gson.annotations.SerializedName
import timeswap.application.data.entity.ChatChoice

data class ChatResponse(

    @SerializedName("choices") val choices: List<ChatChoice>

)