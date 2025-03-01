package timeswap.application.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import timeswap.application.BuildConfig
import timeswap.application.network.services.AuthService
import timeswap.application.network.services.CategoryService
import timeswap.application.network.services.IndustryService
import timeswap.application.network.services.PaymentService
import timeswap.application.network.services.UserService
import timeswap.application.network.services.JobService
import timeswap.application.network.services.LocationService

object RetrofitClient {

    private val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // authRetrofit

    val authService: AuthService by lazy {
        authRetrofit.create(AuthService::class.java)
    }

    val userService: UserService by lazy {
        authRetrofit.create(UserService::class.java)
    }


    // apiRetrofit

    val paymentService: PaymentService by lazy {
        apiRetrofit.create(PaymentService::class.java)
    }

    val jobService: JobService by lazy {
        apiRetrofit.create(JobService::class.java)
    }

    val locationService: LocationService by lazy {
        apiRetrofit.create(LocationService::class.java)
    }

    val industryService: IndustryService by lazy {
        apiRetrofit.create(IndustryService::class.java)
    }

    val categoryService: CategoryService by lazy {
        apiRetrofit.create(CategoryService::class.java)
    }

}
