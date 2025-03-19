package timeswap.application.data.entity

data class User(

    val id: String,
    val email: String,
    val fullName: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val role: List<String>,
    val ward: Ward?,
    val avatarUrl: String,
    val description: String,
    val balance: Double,
    val currentSubscription: Int,
    val subscriptionExpiryDate: String,
    val educationHistory: List<String>,
    val majorCategory: Category?,
    val majorIndustry: Industry?

)