package timeswap.application.data.request

data class UpdateProfileRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val description: String? = null,
    val avatarUrl: String? = null,
    val cityId: String? = null,
    val wardId: String? = null,
    val educationHistory: List<String>? = null,
    val majorCategoryId: Int? = null,
    val majorIndustryId: Int? = null
)
