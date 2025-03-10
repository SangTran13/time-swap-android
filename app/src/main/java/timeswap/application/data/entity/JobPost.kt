package timeswap.application.data.entity

data class JobPost(
    val id: String,
    val userId: String,
    val ownerAvatarUrl: String,
    val ownerName: String,
    val title: String,
    val description: String,
    val responsibilities: String,
    val fee: Double,
    val startDate: String?,
    val dueDate: String?,
    val assignedTo: String?,
    val isOwnerCompleted: Boolean,
    val isAssigneeCompleted: Boolean,
    val category: Category,
    val industry: Industry,
    val ward: Ward,
    val createdAt: String,
    val modifiedAt: String?
)

