package timeswap.application.data.response

data class BaseResponse<T>(

    val statusCode: Int,
    val data: T?,
    val message: String,
    val errors: List<String>?

)