package timeswap.application.network.services

import android.content.SharedPreferences

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

import timeswap.application.data.entity.User
import timeswap.application.data.request.UpdateProfileRequest
import timeswap.application.data.response.BaseResponse
import timeswap.application.network.RetrofitClient

interface UserService {

    @GET("users/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<BaseResponse<User>>

    @PUT("users/profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String, @Body updateProfileRequest: UpdateProfileRequest
    ): Response<BaseResponse<User>>

}

class UserRepository {
    suspend fun getUserProfile(accessToken: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.userService.getUserProfile("Bearer $accessToken")
                if (response.isSuccessful && response.body()?.statusCode == 1000) {
                    response.body()?.data
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun updateUserProfile(accessToken: String, request: UpdateProfileRequest): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    RetrofitClient.userService.updateUserProfile("Bearer $accessToken", request)
                response.isSuccessful && response.body()?.statusCode == 1000
            } catch (e: Exception) {
                false
            }
        }
    }

    fun logout(sharedPreferences: SharedPreferences) {
        with(sharedPreferences.edit()) {
            remove("accessToken")
            remove("refreshToken")
            remove("expiresAt")
            apply()
        }
    }
}
