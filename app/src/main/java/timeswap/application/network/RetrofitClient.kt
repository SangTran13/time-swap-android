package timeswap.application.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import timeswap.application.network.services.AuthService
import timeswap.application.network.services.UserService

object RetrofitClient {

    private const val BASE_URL = "https://tranduchuy.me:9001/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

}
