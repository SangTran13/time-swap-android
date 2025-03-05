package timeswap.application.network.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timeswap.application.BuildConfig

object HttpClientProvider {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.KEY_GPT_API}")
                    .addHeader("OpenAI-Organization", BuildConfig.KEY_ORGANIZATION_GPT_API)
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()
    }
}
