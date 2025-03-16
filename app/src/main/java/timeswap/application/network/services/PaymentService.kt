package timeswap.application.network.services

import android.content.Context
import android.content.Intent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

import timeswap.application.WebViewActivity
import timeswap.application.data.request.PaymentRequest
import timeswap.application.data.response.BaseResponse
import timeswap.application.network.RetrofitClient
import timeswap.application.shared.constants.StatusCodeConstants

interface PaymentService {

    @POST("payments")
    suspend fun createPayment(
        @Header("Authorization") authToken: String, @Body request: PaymentRequest
    ): Response<BaseResponse<String>>

    @GET("payments/payos-return")
    suspend fun verifyPayment(
        @Query("status") status: String,
        @Query("code") code: String,
        @Query("id") paymentId: String,
        @Query("orderCode") orderCode: String,
        @Query("cancel") cancel: Boolean
    ): Response<BaseResponse<String>>

}

class PaymentRepository(private val context: Context) {

    suspend fun handlePayment(
        token: String?,
        amount: Int,
        note: String,
        paymentMethod: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (token.isNullOrEmpty()) {
            onError("Thiếu thông tin xác thực")
            return
        }

        try {
            withContext(Dispatchers.IO) {
                val request = PaymentRequest(note, amount, paymentMethod)
                val response = RetrofitClient.paymentService.createPayment("Bearer $token", request)

                if (response.isSuccessful) {
                    val baseResponse = response.body()
                    if (baseResponse != null) {
                        if (baseResponse.statusCode == StatusCodeConstants.SUCCESS_CODE && baseResponse.data != null) {
                            openPaymentLink(baseResponse.data)
                            onSuccess()
                        } else {
                            onError(baseResponse.message)
                        }
                    } else {
                        onError("Định dạng không hợp lệ!")
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Lỗi không xác định!"
                    onError("Thanh toán thất bại: $errorBody")
                }
            }
        } catch (e: Exception) {
            onError("Error: ${e.message}")
        }
    }

    private fun openPaymentLink(url: String) {
        val intent = Intent(context, WebViewActivity::class.java).apply {
            putExtra("payment_url", url)
        }
        context.startActivity(intent)
    }

    suspend fun verifyPayment(
        status: String?,
        code: String?,
        paymentId: String?,
        orderCode: String?,
        cancel: Boolean,
        onResult: (Boolean, String) -> Unit
    ) {
        if (status.isNullOrEmpty() || code.isNullOrEmpty() || paymentId.isNullOrEmpty() || orderCode.isNullOrEmpty()) {
            onResult(false, "Không xác định được thông tin thanh toán")
            return
        }

        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.paymentService.verifyPayment(
                    status,
                    code,
                    paymentId,
                    orderCode,
                    cancel
                )

                if (response.isSuccessful) {
                    response.body()?.let { baseResponse ->
                        val isSuccess = baseResponse.statusCode == StatusCodeConstants.SUCCESS_CODE
                        onResult(isSuccess, baseResponse.message)
                    } ?: onResult(false, "Thanh toán thất bại")
                } else {
                    onResult(false, "Lỗi: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onResult(false, "Lỗi: ${e.message}")
            }
        }
    }

}