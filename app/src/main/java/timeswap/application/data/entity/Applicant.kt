package timeswap.application.data.entity

data class Applicant(
    val userId: String,
    val jobPostId: String,
    val fullName: String,
    val avatarUrl: String,
    val email: String,
    val appliedAt: String
)
