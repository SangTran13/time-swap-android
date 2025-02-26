package timeswap.application.data.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)
