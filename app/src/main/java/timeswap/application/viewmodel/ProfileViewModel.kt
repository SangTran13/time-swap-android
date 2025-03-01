package timeswap.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import timeswap.application.data.entity.User
import timeswap.application.network.services.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> get() = _userProfile

    fun loadUserProfile(accessToken: String) {
        viewModelScope.launch {
            val user = userRepository.getUserProfile(accessToken)
            _userProfile.value = user
        }
    }
}