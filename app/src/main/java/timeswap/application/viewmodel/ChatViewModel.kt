package timeswap.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timeswap.application.data.entity.ChatMessage
import timeswap.application.network.services.ChatRepository

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    fun sendMessage(userMessage: String) {
        val updatedMessages = _chatMessages.value + ChatMessage(role = "user", content = userMessage)
        _chatMessages.value = updatedMessages

        viewModelScope.launch {
            val botResponse = repository.sendMessage(updatedMessages)
            _chatMessages.value += ChatMessage(
                role = "assistant",
                content = botResponse ?: "Không nhận được phản hồi."
            )
        }
    }

}