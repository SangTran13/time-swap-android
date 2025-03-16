package timeswap.application.network.services

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import timeswap.application.BuildConfig
import timeswap.application.data.request.ChangePasswordRequest
import timeswap.application.data.request.ForgotPasswordRequest
import timeswap.application.data.request.LoginRequest
import timeswap.application.data.request.RegisterRequest
import timeswap.application.data.response.BaseResponse
import timeswap.application.data.response.LoginResponse
import timeswap.application.network.RetrofitClient.authService
import timeswap.application.shared.constants.HttpResponseCodeConstants.Companion.UNAUTHORIZED
import timeswap.application.shared.constants.StatusCodeConstants
import timeswap.application.shared.constants.StatusCodeConstants.Companion.USER_NOT_CONFIRMED
import timeswap.application.ui.utils.ApiUtils

interface AuthService {

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<BaseResponse<LoginResponse>>

    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<BaseResponse<Unit>>

    @POST("auth/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<BaseResponse<Unit>>

    @POST("auth/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Response<BaseResponse<Any>>

}

class ForgotPasswordService {

    fun forgotPassword(
        email: String, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        val request = ForgotPasswordRequest(email)

        authService.forgotPassword(request)
            .enqueue(object : Callback<BaseResponse<Unit>> {
                override fun onResponse(
                    call: Call<BaseResponse<Unit>>, response: Response<BaseResponse<Unit>>
                ) {
                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        onError("Địa chỉ email không tồn tại!")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                    onError("Không thể kết nối đến máy chủ!")
                }
            })
    }
}

class AuthServices(private val context: Context, private val sharedPreferences: SharedPreferences) {

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val request = LoginRequest(email.trim(), password.trim())

        authService.login(request)
            .enqueue(object : Callback<BaseResponse<LoginResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<LoginResponse>>,
                    response: Response<BaseResponse<LoginResponse>>
                ) {
                    if (response.isSuccessful) {
                        val statusCode = response.body()?.statusCode

                        if (statusCode == StatusCodeConstants.SUCCESS_CODE) {
                            val authData = response.body()?.data
                            authData?.let {
                                saveTokens(it.accessToken, it.refreshToken, it.expiresIn)
                                Toast.makeText(context, "Đăng nhập thành công!", LENGTH_SHORT)
                                    .show()
                                onSuccess()
                            }
                        } else {
                            onError("Sai tài khoản hoặc mật khẩu!!")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val statusCode = ApiUtils.extractStatusCode(errorBody)

                        if (response.code() == UNAUTHORIZED && statusCode == USER_NOT_CONFIRMED) {
                            onError("Vui lòng xác nhận email trước khi đăng nhập.")
                        } else {
                            onError("Sai tài khoản hoặc mật khẩu!!")
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<LoginResponse>>, t: Throwable) {
                    onError("Error: ${t.message}")
                }
            })
    }

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val request = RegisterRequest(
            firstName.trim(),
            lastName.trim(),
            email.trim(),
            phoneNumber.trim(),
            password.trim(),
            confirmPassword.trim(),
            clientUrl = BuildConfig.CONFIRM_EMAIL_AUTH_URL
        )

        authService.register(request).enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>, response: Response<BaseResponse<Unit>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Kiểm tra email để xác nhận tài khoản", Toast.LENGTH_LONG)
                        .show()
                    onSuccess()
                } else {
                    val errorMessage = try {
                        JSONObject(response.errorBody()?.string() ?: "{}").optString(
                            "message", "Lỗi đăng ký"
                        )
                    } catch (e: Exception) {
                        "Lỗi đăng ký"
                    }
                    onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                onError("Không thể kết nối đến máy chủ!")
            }
        })
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        val token = sharedPreferences.getString("accessToken", "") ?: ""
        if (token.isBlank()) return Result.failure(Exception("Người dùng chưa đăng nhập"))

        return withContext(Dispatchers.IO) {
            try {
                val response = authService.changePassword(
                    "Bearer $token",
                    ChangePasswordRequest(currentPassword, newPassword)
                )
                if (response.isSuccessful && response.body()?.statusCode == 1005) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Không thể thay đổi mật khẩu!!"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Lỗi khi thay đổi mật khẩu: ${e.message}"))
            }
        }
    }

    private fun saveTokens(accessToken: String, refreshToken: String, expiresIn: Int) {
        with(sharedPreferences.edit()) {
            putString("accessToken", accessToken)
            putString("refreshToken", refreshToken)
            putLong("expiresAt", System.currentTimeMillis() + expiresIn * 1000)
            apply()
        }
    }

}