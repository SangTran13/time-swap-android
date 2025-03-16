package timeswap.application.data.request

data class ForgotPasswordRequest(

    val email: String,
    val clientUrl: String = "https://swap-time.vercel.app/auth/reset-password"

)