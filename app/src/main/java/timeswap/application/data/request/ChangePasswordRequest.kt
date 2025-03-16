package timeswap.application.data.request

data class ChangePasswordRequest(

    val currentPassword: String,
    val newPassword: String

)