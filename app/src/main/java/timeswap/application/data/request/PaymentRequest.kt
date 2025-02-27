package timeswap.application.data.request

data class PaymentRequest(
    val paymentContent: String,
    val amount: Int,
    val paymentMethodId: Int
)
