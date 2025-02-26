package timeswap.application.data.request

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val confirmPassword: String,
    val clientUrl: String = "https://tranduchuy.me:9001/api/auth/confirm-email"
)
