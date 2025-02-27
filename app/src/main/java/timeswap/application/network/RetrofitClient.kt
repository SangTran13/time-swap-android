package timeswap.application.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import timeswap.application.network.services.AuthService
import timeswap.application.network.services.PaymentService
import timeswap.application.network.services.UserService

object RetrofitClient {

    private const val BASE_AUTH_URL = "https://tranduchuy.me:9001/api/"
    private const val BASE_API_URL = "https://tranduchuy.me:9002/api/"

    private val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        authRetrofit.create(AuthService::class.java)
    }

    val userService: UserService by lazy {
        authRetrofit.create(UserService::class.java)
    }

    val paymentService: PaymentService by lazy {
        apiRetrofit.create(PaymentService::class.java)
    }

}
