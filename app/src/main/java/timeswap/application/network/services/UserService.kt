package timeswap.application.network.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

import timeswap.application.data.entity.User
import timeswap.application.data.response.BaseResponse

interface UserService {
    @GET("users/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<BaseResponse<User>>
}
