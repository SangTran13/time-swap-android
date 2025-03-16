package timeswap.application.data.request

data class JobPostRequest(

    val title: String,
    val description: String,
    val responsibilities: String,
    val fee: Double,
    val startDate: String?,
    val dueDate: String?,
    val categoryId: Int,
    val industryId: Int,
    val wardId: String,
    val cityId: String

)