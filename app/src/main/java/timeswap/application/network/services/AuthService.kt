package timeswap.application.network.services

import android.widget.Toast

import org.json.JSONObject

import android.content.Context
import android.content.SharedPreferences

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
import timeswap.application.shared.constants.HttpResponseCodeConstants
import timeswap.application.shared.constants.StatusCodeConstants
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
                        onError("Email does not exist!")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                    onError("Can't connect to server!")
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
                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT)
                                    .show()
                                onSuccess()
                            }
                        } else {
                            onError("Invalid credentials.")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val statusCode = ApiUtils.extractStatusCode(errorBody)

                        if (response.code() == HttpResponseCodeConstants.UNAUTHORIZED && statusCode == StatusCodeConstants.USER_NOT_CONFIRMED) {
                            onError("Please confirm your email before logging in.")
                        } else {
                            onError("Invalid credentials.")
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
                    Toast.makeText(context, "Check your email to confirm!", Toast.LENGTH_LONG)
                        .show()
                    onSuccess()
                } else {
                    val errorMessage = try {
                        JSONObject(response.errorBody()?.string() ?: "{}").optString(
                            "message", "Unknown error"
                        )
                    } catch (e: Exception) {
                        "Unexpected error occurred!"
                    }
                    onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, t: Throwable) {
                onError("Cannot connect to server. Check your internet!")
            }
        })
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        val token = sharedPreferences.getString("accessToken", "") ?: ""
        if (token.isBlank()) return Result.failure(Exception("User is not authenticated"))

        return withContext(Dispatchers.IO) {
            try {
                val response = authService.changePassword("Bearer $token", ChangePasswordRequest(currentPassword, newPassword))
                if (response.isSuccessful && response.body()?.statusCode == 1005) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(response.body()?.message ?: "Password change failed"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Network error: ${e.message}"))
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
