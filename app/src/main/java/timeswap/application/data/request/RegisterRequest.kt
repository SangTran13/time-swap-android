package timeswap.application.data.request

import timeswap.application.BuildConfig

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val confirmPassword: String,
    val clientUrl: String = BuildConfig.CONFIRM_EMAIL_AUTH_URL
)
